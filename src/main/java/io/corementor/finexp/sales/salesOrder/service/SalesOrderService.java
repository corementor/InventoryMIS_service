package io.corementor.finexp.sales.salesOrder.service;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.common.*;
import io.corementor.finexp.inventory.productType.domain.ProductTypeEntity;
import io.corementor.finexp.inventory.productType.service.ProductTypeQueryService;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.sales.salesOrder.domain.SalesOrderItemEntity;
import io.corementor.finexp.sales.salesOrder.domain.SalesOrderEntity;
import io.corementor.finexp.sales.salesOrder.repository.ISalesOrderRepository;
import io.corementor.finexp.sales.salesOrderHistory.service.SalesOrderHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * The Class sale order service
 *
 * @author BLAISE MUGISHA
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SalesOrderService {
    /**
     * The Sequence Number Generator Util
     */
    private final SequenceNumberGeneratorUtil sequenceNumberGeneratorUtil;
    /**
     * The Product Type Query Service
     */
    private final ProductTypeQueryService productTypeQueryService;
    /**
     * The Sales Order Repository
     */
    private final ISalesOrderRepository salesOrderRepository;

    /**
     * The Sales order repository
     */

    private final SalesOrderHistoryService salesOrderHistoryService;
    /**
     * Create sales order
     *
     * @param theSalesOrderEntity the Sales order  entity
     * @return response
     */
    public Response<SalesOrderEntity> createSalesOrder(SalesOrderEntity theSalesOrderEntity) {
        log.info("CREATE SALES ODER METHOD REACHED");
        try {
            if (theSalesOrderEntity == null) return new Response<>(IMessage.INVALID_INPUT);

            String salesOrderNumber = sequenceNumberGeneratorUtil.getIdentifier(
                    ESequenceType.SALES_ORDER, ESequencePrefix.SALES);
            theSalesOrderEntity.setSaleCode(salesOrderNumber);

            if (theSalesOrderEntity.getSaleDate() == null) theSalesOrderEntity.setSaleDate(LocalDate.now());

            BigDecimal totalPrice = BigDecimal.ZERO;

            if (theSalesOrderEntity.getOrderItems() != null && !theSalesOrderEntity.getOrderItems().isEmpty()) {
                for (SalesOrderItemEntity item : theSalesOrderEntity.getOrderItems()) {
                    if (item.getProductType() == null || item.getProductType().getId() == null) {
                        throw new IllegalArgumentException("Product type ID is required for order items");
                    }
                    Response<ProductTypeEntity> productTypeResponse = productTypeQueryService.findProductTypeById(item.getProductType().getId());

                    if (productTypeResponse.getData() == null) {
                        throw new IllegalArgumentException("Product type not found: " + item.getProductType().getId());
                    }
                    ProductTypeEntity productType = productTypeResponse.getData();

                    item.setProductName(productType.getProductName());
                    item.setSize(productType.getSize());
                    item.setSaleOrderEntity(theSalesOrderEntity);

                    if (item.getUnitPrice() == null) item.setUnitPrice(productType.getSellUnitPrice());

                    if (item.getUnitPrice() == null || item.getQuantity() <= 0) {
                        throw new IllegalArgumentException("Invalid unit price or quantity for product: " + productType.getProductName());
                    }
                    BigDecimal itemTotalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    item.setTotalPrice(itemTotalPrice);
                    totalPrice = totalPrice.add(itemTotalPrice);

                    item.setCreatedAt(LocalDateTime.now());
                }
            }
            theSalesOrderEntity.setTotalPrice(totalPrice);
            theSalesOrderEntity.setCreatedAt(LocalDateTime.now());
            theSalesOrderEntity.setModifiedAt(LocalDateTime.now());
            theSalesOrderEntity.setStatus(EOrderHistoryStatus.CREATED);

            SalesOrderEntity savedOrder = salesOrderRepository.save(theSalesOrderEntity);
            salesOrderHistoryService.createSalesOrderHistory(savedOrder,"INITIAL SALE HISTORY CREATED");

            return new Response<>(savedOrder, IMessage.INFORMATION_SAVED);

        } catch (Exception ex) {
            log.error("Error creating sales order with items: {}", ex.getMessage(), ex);
            return new Response<>(null, IMessage.INFORMATION_NOT_UPDATED);
        }

    }
    /**
     * Delete sales order
     *
     * @param theSalesOrder the sales order
     * @return response
     */
    public Response<SalesOrderEntity> deleteSalesOrder(SalesOrderEntity theSalesOrder) {
        try {
            SalesOrderEntity existingOrder = salesOrderRepository.findById(theSalesOrder.getId())
                    .orElse(null);
            if (existingOrder == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }
            theSalesOrder.setState(EEntityLifeCycle.INACTIVE);
            SalesOrderEntity updatedOrder = salesOrderRepository.save(theSalesOrder);

            return new Response<>(updatedOrder, IUserMessage.INFORMATION_DELETED);
        } catch (Exception e) {
            log.error("Error deleting sales order {} ", e.getMessage());
            return new Response<>(IUserMessage.ERROR);
        }
    }
    /**
     * Create updatePurchaseOrderWithItems
     *
     * @param theSalesOrder the Sales order  entity
     * @return response
     */
    public Response<SalesOrderEntity> updateSalesOrderWithItems(SalesOrderEntity theSalesOrder) {
        try {
            if (theSalesOrder == null || theSalesOrder.getId() == null) {
                return new Response<>(IMessage.INVALID_INPUT);
            }

            // Find existing purchase order with items
            SalesOrderEntity existingOrder = salesOrderRepository.findById(theSalesOrder.getId())
                    .orElse(null);

            if (existingOrder == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }

            if (theSalesOrder.getSaleDate() != null) {
                existingOrder.setSaleDate(theSalesOrder.getSaleDate());
            }

            // Update purchase code with uniqueness check
            if (theSalesOrder.getSaleCode() != null &&
                    !theSalesOrder.getSaleCode().equals(existingOrder.getSaleCode())) {
                Optional<SalesOrderEntity> existingWithSameCode = salesOrderRepository
                        .findBySaleCode(theSalesOrder.getSaleCode());
                if (existingWithSameCode.isPresent() &&
                        !existingWithSameCode.get().getId().equals(existingOrder.getId())) {
                    return new Response<>(IUserMessage.ENTRY_ALREADY_EXISTS);
                }
                existingOrder.setSaleCode(theSalesOrder.getSaleCode());
            }

            // Handle order items updates if provided
            if (theSalesOrder.getOrderItems() != null) {
                updateOrderItemsProperly(existingOrder, theSalesOrder.getOrderItems());

                // Recalculate total price based on updated items
                BigDecimal newTotal = calculateTotalPrice(existingOrder);
                existingOrder.setTotalPrice(newTotal);
            }

            existingOrder.setModifiedAt(LocalDateTime.now());

            SalesOrderEntity updatedOrder = salesOrderRepository.save(existingOrder);
            return new Response<>(updatedOrder, IMessage.INFORMATION_UPDATED);


        } catch (Exception ex) {
            log.error("Error updating sales order with items: {}", ex.getMessage(), ex);
            return new Response<>(null, IMessage.INFORMATION_NOT_UPDATED);

        }

    }

    /**
     * update order items properly
     *
     * @param existingOrder SalesOrderEntity
     * @param newItems      List<SalesOrderItemEntity>
     */
    private void updateOrderItemsProperly(SalesOrderEntity existingOrder, List<SalesOrderItemEntity> newItems) {

        Map<UUID, SalesOrderItemEntity> existingItemsMap = existingOrder.getOrderItems().stream()
                .filter(item -> item.getId() != null)
                .collect(Collectors.toMap(SalesOrderItemEntity::getId, Function.identity()));

        List<SalesOrderItemEntity> itemsToKeep = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (SalesOrderItemEntity newItem : newItems) {
            SalesOrderItemEntity itemToSave;

            if (newItem.getId() != null && existingItemsMap.containsKey(newItem.getId())) {
                // Update existing item
                itemToSave = existingItemsMap.get(newItem.getId());
                updateExistingItem(itemToSave, newItem);
            } else {
                // Create new item
                itemToSave = createNewItem(existingOrder, newItem);
            }

            // Calculate item total
            BigDecimal itemTotal = itemToSave.getUnitPrice()
                    .multiply(BigDecimal.valueOf(itemToSave.getQuantity()));

            itemToSave.setTotalPrice(itemTotal);
            totalPrice = totalPrice.add(itemTotal);

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
     * @param existing SalesOrderItemEntity
     * @param newData  SalesOrderItemEntity
     */

    private void updateExistingItem(SalesOrderItemEntity existing, SalesOrderItemEntity newData) {
        existing.setQuantity(newData.getQuantity());
        existing.setUnitPrice(newData.getUnitPrice());
        existing.setProductName(newData.getProductName());
        existing.setSize(newData.getSize());
        existing.setModifiedAt(LocalDateTime.now());

        // Update product type if changed
        if (newData.getProductType() != null && newData.getProductType().getId() != null) {
            existing.setProductType(newData.getProductType());
        }
    }

    /**
     * create new item
     *
     * @param salesOrder SalesOrderEntity
     * @param newItem    SalesOrderItemEntity
     * @return SalesOrderItemEntity
     */
    private SalesOrderItemEntity createNewItem(SalesOrderEntity salesOrder, SalesOrderItemEntity newItem) {
        SalesOrderItemEntity item = new SalesOrderItemEntity();
        item.setQuantity(newItem.getQuantity());
        item.setUnitPrice(newItem.getUnitPrice());
        item.setProductName(newItem.getProductName());
        item.setSize(newItem.getSize());
        item.setProductType(newItem.getProductType());
        item.setSaleOrderEntity(salesOrder);
        item.setCreatedAt(LocalDateTime.now());
        return item;
    }

    /**
     * calculate total price
     *
     * @param order SalesOrderEntity
     * @return BigDecimal
     */
    private BigDecimal calculateTotalPrice(SalesOrderEntity order) {
        return order.getOrderItems().stream()
                .filter(item -> item.getState() == null || !EEntityLifeCycle.INACTIVE.equals(item.getState()))
                .map(item -> {
                    // Calculate item total: unitPrice * quantity
                    return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum all item totals
    }


    /**
     * Delete sales order item
     *
     * @param theId the UUID
     * @return response
     */
    public Response<SalesOrderEntity> deleteSalesOrderItem(UUID theId) {
        try {
            if (theId == null) {
                return new Response<>(IMessage.INVALID_INPUT);

            }

            Optional<SalesOrderEntity> optionalSalesOrder =
                    salesOrderRepository.findSalesOrderByProductOrderItemEntity(theId);


            if (optionalSalesOrder.isEmpty()) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }

            SalesOrderEntity order = optionalSalesOrder.get();

            SalesOrderItemEntity targetItem = order.getOrderItems().stream()
                    .filter(i-> theId.equals(i.getId()))
                    .findFirst()
                    .orElse(null);

            if(targetItem==null){
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }

            targetItem.setState(EEntityLifeCycle.INACTIVE);
            targetItem.setModifiedAt(LocalDateTime.now());

            BigDecimal newTotal=calculateTotalPrice(order);
            order.setTotalPrice(newTotal);
            order.setModifiedAt(LocalDateTime.now());

            SalesOrderEntity saved=salesOrderRepository.save(order);
            return new Response<>(saved,IMessage.INFORMATION_UPDATED);


        } catch (Exception e) {
            log.error("Error deleting sales order item: {}", e.getMessage(), e);
            return new Response<>(IUserMessage.ERROR);
        }
    }



    /**
     * Submit for Approval
     *
     * @param requestDto the Request dto
     * @return response
     */
    public Response<SalesOrderEntity> submitForApproval(RequestDto requestDto) {
        try {
            SalesOrderEntity existingOrder = salesOrderRepository.findById(requestDto.getId())
                    .orElse(null);
            if (existingOrder == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }

            if (existingOrder.getStatus() != EOrderHistoryStatus.CREATED &&
                    existingOrder.getStatus() != EOrderHistoryStatus.RETURNED) {
                return new Response<>("Cannot submit order. Current status: " + existingOrder.getStatus());
            }


            existingOrder.setStatus(EOrderHistoryStatus.SUBMITTED);
            existingOrder.setModifiedAt(LocalDateTime.now());
            SalesOrderEntity updatedOrder = salesOrderRepository.save(existingOrder);


            salesOrderHistoryService.createSalesOrderHistory(updatedOrder, requestDto.getComment());

            return new Response<>(updatedOrder, IMessage.INFORMATION_UPDATED);

        } catch (Exception ex) {
            log.error("Error submitting purchase order for approval: {}", ex.getMessage(), ex);
            return new Response<>(IMessage.INFORMATION_NOT_UPDATED);
        }
    }

    /**
     * Approve SalesOrder
     *
     * @param requestDto the RequestDto
     * @return response
     */
    public Response<SalesOrderEntity> approveSalesOrder(RequestDto requestDto) {
        try {
            SalesOrderEntity existingOrder = salesOrderRepository.findById(requestDto.getId())
                    .orElse(null);
            if (existingOrder == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }


            if (existingOrder.getStatus() != EOrderHistoryStatus.SUBMITTED) {
                return new Response<>("Cannot approve order. Current status: " + existingOrder.getStatus());
            }


            existingOrder.setStatus(EOrderHistoryStatus.APPROVED);
            existingOrder.setModifiedAt(LocalDateTime.now());
            SalesOrderEntity updatedOrder = salesOrderRepository.save(existingOrder);


            String comment = "Order approved by manager";
            if (requestDto.getComment() != null && !requestDto.getComment().trim().isEmpty()) {
                comment += ". Comment: " + requestDto.getComment();
            }
            salesOrderHistoryService.createSalesOrderHistory(updatedOrder, comment);

            return new Response<>(updatedOrder, IMessage.INFORMATION_UPDATED);

        } catch (Exception ex) {
            log.error("Error approving purchase order: {}", ex.getMessage(), ex);
            return new Response<>(IMessage.INFORMATION_NOT_UPDATED);
        }
    }

    /**
     * Return SalesOrder
     *
     * @param requestDto the RequestDto
     * @return response
     */
    public Response<SalesOrderEntity> returnSalesOrder(RequestDto requestDto) {
        try {
            SalesOrderEntity existingOrder = salesOrderRepository.findById(requestDto.getId())
                    .orElse(null);
            if (existingOrder == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }


            if (existingOrder.getStatus() != EOrderHistoryStatus.SUBMITTED) {
                return new Response<>("Cannot return order. Current status: " + existingOrder.getStatus());
            }

            if (requestDto.getComment() == null || requestDto.getComment().trim().isEmpty()) {
                return new Response<>("Return reason is required");
            }


            existingOrder.setStatus(EOrderHistoryStatus.RETURNED);
            existingOrder.setModifiedAt(LocalDateTime.now());
            SalesOrderEntity updatedOrder = salesOrderRepository.save(existingOrder);


            salesOrderHistoryService.createSalesOrderHistory(updatedOrder,
                    "Order returned by manager. Reason: " + requestDto.getComment());

            return new Response<>(updatedOrder, IMessage.INFORMATION_UPDATED);

        } catch (Exception ex) {
            log.error("Error returning purchase order: {}", ex.getMessage(), ex);
            return new Response<>(IMessage.INFORMATION_NOT_UPDATED);
        }
    }

}
