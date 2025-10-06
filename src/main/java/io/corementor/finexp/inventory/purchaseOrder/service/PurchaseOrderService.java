package io.corementor.finexp.inventory.purchaseOrder.service;


import io.corementor.finexp.inventory.base.IMessage;
import io.corementor.finexp.inventory.common.util.ESequencePrefix;
import io.corementor.finexp.inventory.common.util.ESequenceType;
import io.corementor.finexp.inventory.common.util.SequenceNumberGeneratorUtil;
import io.corementor.finexp.inventory.productOrderItem.domain.ProductOrderItemEntity;
import io.corementor.finexp.inventory.productType.domain.ProductTypeEntity;
import io.corementor.finexp.inventory.productType.service.ProductTypeQueryService;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.inventory.productOrderItem.service.ProductOrderItemQueryService;
import io.corementor.finexp.inventory.purchaseOrder.repository.IPurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.response.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Create purchase order with items
     */
    public Response<PurchaseOrderEntity> createPurchaseOrder(PurchaseOrderEntity thePurchaseOrderEntity) {
        try {
            if (thePurchaseOrderEntity == null || thePurchaseOrderEntity.getOrderItems() == null || thePurchaseOrderEntity.getOrderItems().isEmpty()) {
                return new Response<>(IMessage.INVALID_INPUT);
            }

            // Generate purchase order number
            String purchaseOrderNumber = sequenceNumberGeneratorUtil.getIdentifier(
                    ESequenceType.PURCHASE_ORDER, ESequencePrefix.PO);
            thePurchaseOrderEntity.setPurchaseCode(purchaseOrderNumber);
            thePurchaseOrderEntity.setPurchaseDate(LocalDate.now());

            // Calculate total price and process items
            BigDecimal totalPrice = BigDecimal.ZERO;
            List<ProductOrderItemEntity> managedOrderItems = new ArrayList<>();

            for (ProductOrderItemEntity item : thePurchaseOrderEntity.getOrderItems()) {
                // Validate product type exists
                Response<ProductTypeEntity> productTypeResponse = productTypeQueryService.findProductTypeById(item.getProductType().getId());
                if (productTypeResponse.getData() == null) {
                    return new Response<>("Product type not found: " + item.getProductType().getId());
                }

                ProductTypeEntity productType = productTypeResponse.getData();

                // Set product details
                item.setProductName(productType.getProductName());
                item.setSize(productType.getSize());

                // Use provided unit price or fallback to product type's unit price
                if (item.getUnitPrice() == null) {
                    item.setUnitPrice(productType.getUnitPrice());
                }

                // Calculate item totals
                BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

                // TaxAmount
                BigDecimal itemTax = item.getTaxAmount();
                BigDecimal itemTotalWithTax = itemTotal.add(itemTax);

                item.setTotalTax(itemTax);
                item.setTotalPriceWithTax(itemTotalWithTax);
                item.setPurchaseOrderEntity(thePurchaseOrderEntity);

                totalPrice = totalPrice.add(itemTotalWithTax);
                managedOrderItems.add(item);
            }

            thePurchaseOrderEntity.setTotalPrice(totalPrice);
            thePurchaseOrderEntity.setOrderItems(managedOrderItems);
            thePurchaseOrderEntity.setCreatedAt(LocalDateTime.now());
            thePurchaseOrderEntity.setModifiedAt(LocalDateTime.now());

            PurchaseOrderEntity savedOrder = purchaseOrderRepository.save(thePurchaseOrderEntity);
            return new Response<>(savedOrder, IMessage.INFORMATION_SAVED);

        } catch (Exception ex) {
            log.error("Error creating purchase order: {}", ex.getMessage(), ex);
            return new Response<>(IMessage.INFORMATION_NOT_SAVED);
        }
    }

    /**
     * Update purchase order
     */
    public Response<PurchaseOrderEntity> updatePurchaseOrder(PurchaseOrderEntity thePurchaseOrderEntity) {
        try {
            if (thePurchaseOrderEntity == null || thePurchaseOrderEntity.getId() == null) {
                return new Response<>(IMessage.INVALID_INPUT);
            }

            // Find existing purchase order
            Response<PurchaseOrderEntity> existingResponse = purchaseOrderQueryService.findPurchaseOrderById(thePurchaseOrderEntity.getId());
            if (existingResponse.getData() == null) {
                return new Response<>("Purchase order not found");
            }

            PurchaseOrderEntity existingOrder = existingResponse.getData();

            // Update fields as needed
            existingOrder.setPurchaseDate(thePurchaseOrderEntity.getPurchaseDate());
            existingOrder.setModifiedAt(LocalDateTime.now());

            // Recalculate total if items are updated
            if (thePurchaseOrderEntity.getOrderItems() != null && !thePurchaseOrderEntity.getOrderItems().isEmpty()) {
                // Implementation for updating items would go here
                // This might involve deleting old items and creating new ones
            }

            PurchaseOrderEntity updatedOrder = purchaseOrderRepository.save(existingOrder);
            return new Response<>(updatedOrder, IMessage.INFORMATION_UPDATED);

        } catch (Exception ex) {
            log.error("Error updating purchase order: {}", ex.getMessage(), ex);
            return new Response<>(IMessage.INFORMATION_NOT_UPDATED);
        }
    }
}
