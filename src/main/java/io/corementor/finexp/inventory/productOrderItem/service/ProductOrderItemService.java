package io.corementor.finexp.inventory.productOrderItem.service;

import io.corementor.finexp.inventory.base.IMessage;
import io.corementor.finexp.inventory.productOrderItem.domain.ProductOrderItemEntity;
import io.corementor.finexp.inventory.productType.domain.ProductTypeEntity;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.inventory.productOrderItem.repository.IProductOrderItemRepository;
import io.corementor.finexp.inventory.productType.service.ProductTypeQueryService;
import io.corementor.finexp.inventory.purchaseOrder.repository.IPurchaseOrderRepository;
import io.corementor.finexp.inventory.purchaseOrder.service.PurchaseOrderQueryService;
import io.corementor.finexp.inventory.purchaseOrder.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Class ProductOrderItemService.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOrderItemService {
    private final IProductOrderItemRepository productOrderItemRepository;
    private final ProductTypeQueryService productTypeQueryService;
    private final PurchaseOrderQueryService purchaseOrderQueryService;
    private final PurchaseOrderService purchaseOrderService;
    private final IPurchaseOrderRepository purchaseOrderRepository;

    /**
     * Create product order item (standalone - for adding items to existing PO)
     */
    public Response<ProductOrderItemEntity> createProductOrderItem(ProductOrderItemEntity theProductOrderItem) {
        try {
            if (theProductOrderItem == null) {
                return new Response<>(null, IMessage.INVALID_INPUT);
            }

            // Validate purchase order exists
            Response<PurchaseOrderEntity> purchaseOrderResponse = purchaseOrderQueryService.findPurchaseOrderById(
                    theProductOrderItem.getPurchaseOrderEntity().getId()
            );
            if (purchaseOrderResponse.getData() == null) {
                return new Response<>(null, IUserMessage.INFORMATION_NOT_FOUND);
            }

            // Validate product type exists
            Response<ProductTypeEntity> productTypeResponse = productTypeQueryService.findProductTypeById(
                    theProductOrderItem.getProductType().getId()
            );
            if (productTypeResponse.getData() == null) {
                return new Response<>(null, IUserMessage.INFORMATION_NOT_FOUND);
            }

            ProductTypeEntity productType = productTypeResponse.getData();
            PurchaseOrderEntity purchaseOrder = purchaseOrderResponse.getData();

            // Set product details
            theProductOrderItem.setProductName(productType.getProductName());
            theProductOrderItem.setSize(productType.getSize());
            theProductOrderItem.setProductType(productType);
            theProductOrderItem.setPurchaseOrderEntity(purchaseOrder);

            // Use provided unit price or fallback to product type's unit price
            if (theProductOrderItem.getUnitPrice() == null) {
                theProductOrderItem.setUnitPrice(productType.getUnitPrice());
            }

            // Calculate totals - FIXED: taxAmount is actual amount, not percentage
            BigDecimal itemTotal = theProductOrderItem.getUnitPrice().multiply(BigDecimal.valueOf(theProductOrderItem.getQuantity()));

            // Use taxAmount directly as it's the actual tax amount
            BigDecimal itemTax = theProductOrderItem.getTaxAmount();
            BigDecimal itemTotalWithTax = itemTotal.add(itemTax);

            theProductOrderItem.setTotalTax(itemTax);
            theProductOrderItem.setTotalPriceWithTax(itemTotalWithTax);
            theProductOrderItem.setCreatedAt(LocalDateTime.now());

            ProductOrderItemEntity savedItem = productOrderItemRepository.save(theProductOrderItem);

            // Update purchase order total
            updatePurchaseOrderTotal(purchaseOrder.getId());

            return new Response<>(savedItem, IMessage.INFORMATION_SAVED);

        } catch (Exception ex) {
            log.error("Error creating product order item: {}", ex.getMessage(), ex);
            return new Response<>(IUserMessage.INFORMATION_NOT_SAVED);
        }
    }

    /**
     * Update product order item
     */
    public Response<ProductOrderItemEntity> updateProductOrderItem(ProductOrderItemEntity theProductOrderItem) {
        try {
            if (theProductOrderItem == null || theProductOrderItem.getId() == null) {
                return new Response<>(null, IMessage.INVALID_INPUT);
            }

            // Find existing product order item
            ProductOrderItemEntity existingItem = productOrderItemRepository.findById(theProductOrderItem.getId())
                    .orElseThrow(() -> new ObjectNotFoundException(theProductOrderItem.getId(), "Product order item not found"));

            // Update fields
            existingItem.setQuantity(theProductOrderItem.getQuantity());
            existingItem.setUnitPrice(theProductOrderItem.getUnitPrice());
            existingItem.setTaxAmount(theProductOrderItem.getTaxAmount());
            existingItem.setModifiedAt(LocalDateTime.now());

            // Recalculate totals with the updated values
            BigDecimal itemTotal = existingItem.getUnitPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity()));
            BigDecimal itemTax = existingItem.getTaxAmount(); // Use taxAmount directly
            BigDecimal itemTotalWithTax = itemTotal.add(itemTax);

            existingItem.setTotalTax(itemTax);
            existingItem.setTotalPriceWithTax(itemTotalWithTax);

            ProductOrderItemEntity updatedItem = productOrderItemRepository.save(existingItem);

            // Update purchase order total
            if (existingItem.getPurchaseOrderEntity() != null) {
                updatePurchaseOrderTotal(existingItem.getPurchaseOrderEntity().getId());
            }

            return new Response<>(updatedItem, IMessage.INFORMATION_UPDATED);

        } catch (Exception ex) {
            log.error("Error updating product order item: {}", ex.getMessage(), ex);
            return new Response<>(IUserMessage.ENTITY_IS_NOT_UPDATEABLE);
        }
    }

    /**
     * Update purchase order total after item changes
     */
    /**
     * Update purchase order total after item changes
     */

    private void updatePurchaseOrderTotal(UUID purchaseOrderId) {
        try {
            // Calculate total directly in database
            BigDecimal newTotal = productOrderItemRepository.calculateTotalPriceByPurchaseOrderId(purchaseOrderId);

            if (newTotal == null) {
                newTotal = BigDecimal.ZERO;
            }

            // Get and update purchase order
            PurchaseOrderEntity purchaseOrder = purchaseOrderQueryService.findPurchaseOrderById(purchaseOrderId).getData();
            if (purchaseOrder != null) {
                purchaseOrder.setTotalPrice(newTotal);
                purchaseOrder.setModifiedAt(LocalDateTime.now());
                 purchaseOrderRepository.save(purchaseOrder);

                log.debug("Updated purchase order {} total to: {}", purchaseOrderId, newTotal);
            }

        } catch (Exception ex) {
            log.error("Error updating purchase order total for ID {}: {}", purchaseOrderId, ex.getMessage(), ex);
        }
    }

    /**
     * Delete product order item
     */
    public Response<Boolean> deleteProductOrderItem(UUID itemId) {
        try {
            ProductOrderItemEntity existingItem = productOrderItemRepository.findById(itemId)
                    .orElseThrow(() -> new ObjectNotFoundException(itemId, "Product order item not found"));

            UUID purchaseOrderId = existingItem.getPurchaseOrderEntity().getId();

            productOrderItemRepository.delete(existingItem);

            // Update purchase order total after deletion
            updatePurchaseOrderTotal(purchaseOrderId);

            return new Response<>(true, IUserMessage.INFORMATION_UPDATED);

        } catch (Exception ex) {
            log.error("Error deleting product order item: {}", ex.getMessage(), ex);
            return new Response<>(false, IUserMessage.INFORMATION_NOT_FOUND);
        }
    }
}