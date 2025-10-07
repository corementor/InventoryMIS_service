package io.corementor.finexp.inventory.purchaseOrder.service;


import io.corementor.finexp.inventory.base.IMessage;
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
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private final IPurchaseOrderRepository purchaseOrderRepository;
    private final SequenceNumberGeneratorUtil sequenceNumberGeneratorUtil;
    private final ProductOrderItemQueryService productOrderItemQueryService;
    private final ProductTypeQueryService productTypeQueryService;
    private final PurchaseOrderQueryService purchaseOrderQueryService;
    private final IProductOrderItemRepository productOrderItemRepository;

    /**
     * Create purchase order with items
     */
    public Response<PurchaseOrderEntity> createPurchaseOrder(PurchaseOrderEntity thePurchaseOrderEntity) {
        try {
            if (thePurchaseOrderEntity == null) {
                return new Response<>(null, IMessage.INVALID_INPUT);
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
                    // Validate product type exists
                    if (item.getProductType() == null || item.getProductType().getId() == null) {
                        throw new IllegalArgumentException("Product type ID is required for order items");
                    }

                    Response<ProductTypeEntity> productTypeResponse = productTypeQueryService.findProductTypeById(item.getProductType().getId());
                    if (productTypeResponse.getData() == null ) {
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
                    BigDecimal itemTax = item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO;
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
            return new Response<>( IMessage.INFORMATION_NOT_SAVED);
        } catch (Exception ex) {
            log.error("Error creating purchase order: {}", ex.getMessage(), ex);
            return new Response<>( IMessage.INFORMATION_NOT_SAVED);
        }
    }

    /**
     * Update purchase order
     */
    public Response<PurchaseOrderEntity> updatePurchaseOrderWithItems(PurchaseOrderEntity thePurchaseOrderEntity) {
        try {
            if (thePurchaseOrderEntity == null || thePurchaseOrderEntity.getId() == null) {
                return new Response<>(null, IMessage.INVALID_INPUT);
            }

            // Find existing purchase order with items
            PurchaseOrderEntity existingOrder = purchaseOrderRepository.findById(thePurchaseOrderEntity.getId())
                    .orElse(null);
            if (existingOrder == null) {
                return new Response<>(null, IUserMessage.INFORMATION_NOT_FOUND);
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
                updateOrderItems(existingOrder, thePurchaseOrderEntity.getOrderItems());

                // Recalculate total price based on updated items
                BigDecimal newTotal = calculateTotalPrice(existingOrder);
                existingOrder.setTotalPrice(newTotal);
            }

            existingOrder.setModifiedAt(LocalDateTime.now());

            PurchaseOrderEntity updatedOrder = purchaseOrderRepository.save(existingOrder);
            return new Response<>(updatedOrder, IMessage.INFORMATION_UPDATED);

        } catch (Exception ex) {
            log.error("Error updating purchase order with items: {}", ex.getMessage(), ex);
            return new Response<>(null, IMessage.INFORMATION_NOT_UPDATED);
        }
    }

    private void updateOrderItems(PurchaseOrderEntity existingOrder, List<ProductOrderItemEntity> newItems) {
        // This is a simplified version - you might need more complex logic
        // depending on your requirements (add/update/delete items)

        // Clear existing items and add new ones
        existingOrder.getOrderItems().clear();
        for (ProductOrderItemEntity newItem : newItems) {
            newItem.setPurchaseOrderEntity(existingOrder);
            existingOrder.getOrderItems().add(newItem);
        }
    }

    private BigDecimal calculateTotalPrice(PurchaseOrderEntity order) {
        return order.getOrderItems().stream()
                .map(ProductOrderItemEntity::getTotalPriceWithTax)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}