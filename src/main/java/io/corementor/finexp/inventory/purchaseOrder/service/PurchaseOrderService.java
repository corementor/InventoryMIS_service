package io.corementor.finexp.inventory.purchaseOrder.service;


import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.inventory.common.util.ESequencePrefix;
import io.corementor.finexp.inventory.common.util.ESequenceType;
import io.corementor.finexp.inventory.common.util.SequenceNumberGeneratorUtil;
import io.corementor.finexp.inventory.productOrderItem.domain.ProductOrderItemEntity;
import io.corementor.finexp.inventory.productOrderItem.repository.IProductOrderItemRepository;
import io.corementor.finexp.inventory.productType.domain.ProductTypeEntity;
import io.corementor.finexp.inventory.productType.service.ProductTypeQueryService;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.inventory.productOrderItem.service.ProductOrderItemQueryService;
import io.corementor.finexp.inventory.purchaseOrder.repository.IPurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Class PurchaseOrderService.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    /**
     * The Purchase order repository
     */
    private final IPurchaseOrderRepository purchaseOrderRepository;
    /**
     * The Sequence number generator util
     */
    private final SequenceNumberGeneratorUtil sequenceNumberGeneratorUtil;
    /**
     * The product type query service
     */
    private final ProductTypeQueryService productTypeQueryService;


    /**
     * Create purchase order with items
     */
    public Response<PurchaseOrderEntity> createPurchaseOrder(PurchaseOrderEntity thePurchaseOrderEntity) {
        log.info("CREATE PURCHASE ODER METHOD REACHED");
        try {
            if (thePurchaseOrderEntity == null) {
                return new Response<>(IMessage.INVALID_INPUT);
            }

            // Generate purchase order number
            String purchaseOrderNumber = sequenceNumberGeneratorUtil.getIdentifier(
                    ESequenceType.PURCHASE_ORDER, ESequencePrefix.PO);
            thePurchaseOrderEntity.setPurchaseCode(purchaseOrderNumber);

            if (thePurchaseOrderEntity.getPurchaseDate() == null) {
                thePurchaseOrderEntity.setPurchaseDate(LocalDate.now());
            }

            // Calculate total price and prepare items
            BigDecimal totalPrice = BigDecimal.ZERO;

            if (thePurchaseOrderEntity.getOrderItems() != null && !thePurchaseOrderEntity.getOrderItems().isEmpty()) {
                for (ProductOrderItemEntity item : thePurchaseOrderEntity.getOrderItems()) {

                    if (item.getProductType() == null || item.getProductType().getId() == null) {
                        throw new IllegalArgumentException("Product type ID is required for order items");
                    }



                    Response<ProductTypeEntity> productTypeResponse = productTypeQueryService.findProductTypeById(item.getProductType().getId());
                    if (productTypeResponse.getData() == null) {
                        throw new IllegalArgumentException("Product type not found: " + item.getProductType().getId());
                    }

                    ProductTypeEntity productType = productTypeResponse.getData();

                    // Set product details
                    item.setProductName(productType.getProductName());
                    item.setSize(productType.getSize());
                    item.setPurchaseOrderEntity(thePurchaseOrderEntity);

                    // Use provided unit price or fallback to product type's unit price
                    if (item.getUnitPrice() == null) {
                        item.setUnitPrice(productType.getUnitPrice());
                    }

                    // Validate required fields
                    if (item.getUnitPrice() == null || item.getQuantity() <= 0) {
                        throw new IllegalArgumentException("Invalid unit price or quantity for product: " + productType.getProductName());
                    }

                    // Calculate item totals
                    BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

                    BigDecimal itemTax = item.getTaxAmount() != null ? item.getTaxAmount().multiply(BigDecimal.valueOf(item.getQuantity())) : BigDecimal.ZERO;
                    BigDecimal itemTotalWithTax = itemTotal.add(itemTax);

                    item.setTotalTax(itemTax);
                    item.setTotalPriceWithTax(itemTotalWithTax);
                    item.setCreatedAt(LocalDateTime.now());

                    totalPrice = totalPrice.add(itemTotalWithTax);
                }
            }

            // Set total price (will be ZERO if no items)
            thePurchaseOrderEntity.setTotalPrice(totalPrice);
            thePurchaseOrderEntity.setCreatedAt(LocalDateTime.now());
            thePurchaseOrderEntity.setModifiedAt(LocalDateTime.now());

            // Save everything in one transaction (cascade will save items)
            PurchaseOrderEntity savedOrder = purchaseOrderRepository.save(thePurchaseOrderEntity);

            return new Response<>(savedOrder, IMessage.INFORMATION_SAVED);

        } catch (IllegalArgumentException ex) {
            log.error("Validation error creating purchase order: {}", ex.getMessage());
            return new Response<>(IMessage.INFORMATION_NOT_SAVED);
        } catch (Exception ex) {
            log.error("Error creating purchase order: {}", ex.getMessage(), ex);
            return new Response<>(IMessage.INFORMATION_NOT_SAVED);
        }
    }

    /**
     * Update purchase order with proper item management
     *
     * @param thePurchaseOrderEntity the purchase order
     * @return response
     */
    public Response<PurchaseOrderEntity> updatePurchaseOrderWithItems(PurchaseOrderEntity thePurchaseOrderEntity) {
        try {
            if (thePurchaseOrderEntity == null || thePurchaseOrderEntity.getId() == null) {
                return new Response<>(IMessage.INVALID_INPUT);
            }

            // Find existing purchase order with items
            PurchaseOrderEntity existingOrder = purchaseOrderRepository.findById(thePurchaseOrderEntity.getId())
                    .orElse(null);
            if (existingOrder == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }

            // Update basic fields
            if (thePurchaseOrderEntity.getPurchaseDate() != null) {
                existingOrder.setPurchaseDate(thePurchaseOrderEntity.getPurchaseDate());
            }

            // Update purchase code with uniqueness check
            if (thePurchaseOrderEntity.getPurchaseCode() != null &&
                    !thePurchaseOrderEntity.getPurchaseCode().equals(existingOrder.getPurchaseCode())) {
                Optional<PurchaseOrderEntity> existingWithSameCode = purchaseOrderRepository
                        .findByPurchaseCode(thePurchaseOrderEntity.getPurchaseCode());
                if (existingWithSameCode.isPresent() &&
                        !existingWithSameCode.get().getId().equals(existingOrder.getId())) {
                    return new Response<>(IUserMessage.ENTRY_ALREADY_EXISTS);
                }
                existingOrder.setPurchaseCode(thePurchaseOrderEntity.getPurchaseCode());
            }

            // Handle order items updates if provided
            if (thePurchaseOrderEntity.getOrderItems() != null) {
                updateOrderItemsProperly(existingOrder, thePurchaseOrderEntity.getOrderItems());

                // Recalculate total price based on updated items
                BigDecimal newTotal = calculateTotalPrice(existingOrder);
                existingOrder.setTotalPrice(newTotal);
            }

            existingOrder.setModifiedAt(LocalDateTime.now());

            PurchaseOrderEntity updatedOrder = purchaseOrderRepository.save(existingOrder);
            return new Response<>(updatedOrder, IMessage.INFORMATION_UPDATED);

        } catch (Exception ex) {
            log.error("Error updating purchase order with items: {}", ex.getMessage(), ex);
            return new Response<>(IMessage.INFORMATION_NOT_UPDATED);
        }
    }

    /**
     * Properly update order items without losing data
     * updateOrderItemsProperly
     *
     * @param existingOrder the Purchase order entity
     * @param newItems      the product item list
     */
    private void updateOrderItemsProperly(PurchaseOrderEntity existingOrder, List<ProductOrderItemEntity> newItems) {

        Map<UUID, ProductOrderItemEntity> existingItemsMap = existingOrder.getOrderItems().stream()
                .filter(item -> item.getId() != null)
                .collect(Collectors.toMap(ProductOrderItemEntity::getId, Function.identity()));

        List<ProductOrderItemEntity> itemsToKeep = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (ProductOrderItemEntity newItem : newItems) {
            ProductOrderItemEntity itemToSave;

            if (newItem.getId() != null && existingItemsMap.containsKey(newItem.getId())) {
                // Update existing item
                itemToSave = existingItemsMap.get(newItem.getId());
                updateExistingItem(itemToSave, newItem);
            } else {
                // Create new item
                itemToSave = createNewItem(existingOrder, newItem);
            }

            BigDecimal itemTotal = itemToSave.getUnitPrice()
                    .multiply(BigDecimal.valueOf(itemToSave.getQuantity()));

            BigDecimal itemTax = itemToSave.getTaxAmount() != null ?
                    itemToSave.getTaxAmount().multiply(BigDecimal.valueOf(itemToSave.getQuantity())) :
                    BigDecimal.ZERO;

            BigDecimal itemTotalWithTax = itemTotal.add(itemTax);

            itemToSave.setTotalTax(itemTax);
            itemToSave.setTotalPriceWithTax(itemTotalWithTax);

            totalPrice = totalPrice.add(itemTotalWithTax);

            itemsToKeep.add(itemToSave);
        }
        // Set the updated items list
        existingOrder.getOrderItems().clear();
        existingOrder.getOrderItems().addAll(itemsToKeep);
        existingOrder.setTotalPrice(totalPrice);
    }

    /**
     * update existing item
     *
     * @param existing the productOrderItemEntity
     * @param newData  ProductOrderItemEntity
     */
    private void updateExistingItem(ProductOrderItemEntity existing, ProductOrderItemEntity newData) {
        existing.setQuantity(newData.getQuantity());
        existing.setUnitPrice(newData.getUnitPrice());
        existing.setTaxAmount(newData.getTaxAmount());
        existing.setProductName(newData.getProductName());
        existing.setSize(newData.getSize());
        existing.setModifiedAt(LocalDateTime.now());

        // Update product type if changed
        if (newData.getProductType() != null && newData.getProductType().getId() != null) {
            existing.setProductType(newData.getProductType());
        }
    }

    /**
     * Create new Item
     *
     * @param purchaseOrder the Purchase order Entity
     * @param newItem       the ProductOrder Item Entity
     * @return the ProductOrderItemEntity
     */
    private ProductOrderItemEntity createNewItem(PurchaseOrderEntity purchaseOrder, ProductOrderItemEntity newItem) {
        ProductOrderItemEntity item = new ProductOrderItemEntity();
        item.setQuantity(newItem.getQuantity());
        item.setUnitPrice(newItem.getUnitPrice());
        item.setTaxAmount(newItem.getTaxAmount());
        item.setProductName(newItem.getProductName());
        item.setSize(newItem.getSize());
        item.setProductType(newItem.getProductType());
        item.setPurchaseOrderEntity(purchaseOrder);
        item.setCreatedAt(LocalDateTime.now());
        return item;
    }

    /**
     * calculate total price
     *
     * @param order PurchaseOrderEntity
     * @return BigDecimal
     */

    private BigDecimal calculateTotalPrice(PurchaseOrderEntity order) {
        return order.getOrderItems().stream()
                .filter(item -> item.getState() == null || !EEntityLifeCycle.INACTIVE.equals(item.getState()))
                .map(item -> {
                    BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    BigDecimal itemTax = item.getTaxAmount() != null ? item.getTaxAmount().multiply(BigDecimal.valueOf(item.getQuantity())) : BigDecimal.ZERO;
                    return itemTotal.add(itemTax);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Delete purchase order
     *
     * @param thePurchaseOrder the purchase order
     * @return response
     */
    public Response<PurchaseOrderEntity> deletePurchaseOrder(PurchaseOrderEntity thePurchaseOrder) {
        try {
            PurchaseOrderEntity existingOrder = purchaseOrderRepository.findById(thePurchaseOrder.getId())
                    .orElse(null);
            if (existingOrder == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }
            thePurchaseOrder.setState(EEntityLifeCycle.INACTIVE);
            PurchaseOrderEntity updatedOrder = purchaseOrderRepository.save(thePurchaseOrder);

            return new Response<>(updatedOrder, IUserMessage.INFORMATION_DELETED);
        } catch (Exception e) {
            log.error("Error deleting purchase order {} ", e.getMessage());
            return new Response<>(IUserMessage.ERROR);
        }
    }

    /**
     * Delete product order item
     *
     * @param theId the UUID
     * @return response
     */
    public Response<PurchaseOrderEntity> deletePurchaseOrderItem(UUID theId) {
        try {
            if (theId == null) {
                return new Response<>(IMessage.INVALID_INPUT);
            }

            Optional<PurchaseOrderEntity> optionalOrder =
                    purchaseOrderRepository.findPurchaseOrderEntityByProductOrderItemEntity(theId);

            if (optionalOrder.isEmpty()) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }

            PurchaseOrderEntity order = optionalOrder.get();

            ProductOrderItemEntity targetItem = order.getOrderItems().stream()
                    .filter(i -> theId.equals(i.getId()))
                    .findFirst()
                    .orElse(null);

            if (targetItem == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }


            targetItem.setState(EEntityLifeCycle.INACTIVE);
            targetItem.setModifiedAt(LocalDateTime.now());

            BigDecimal newTotal = calculateTotalPrice(order);
            order.setTotalPrice(newTotal);
            order.setModifiedAt(LocalDateTime.now());

            PurchaseOrderEntity saved = purchaseOrderRepository.save(order);
            return new Response<>(saved, IUserMessage.INFORMATION_DELETED);

        } catch (Exception e) {
            log.error("Error deleting purchase order item {} ", e.getMessage());
            return new Response<>(IUserMessage.ERROR);
        }
    }
}