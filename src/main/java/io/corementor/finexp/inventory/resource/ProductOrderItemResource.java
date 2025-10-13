package io.corementor.finexp.inventory.resource;

import io.corementor.finexp.inventory.productOrderItem.domain.ProductOrderItemEntity;
import io.corementor.finexp.inventory.productOrderItem.service.ProductOrderItemQueryService;
import io.corementor.finexp.inventory.productOrderItem.service.ProductOrderItemService;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class ProductOrderItemResource.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/productOrderItem")
public class ProductOrderItemResource {
    /**
     *The ProductOrderItemService
     */
    private final ProductOrderItemService productOrderItemService;

    /**
     * The productOrderItemQueryService
     */
    private final ProductOrderItemQueryService productOrderItemQueryService;

    /**
     * Create product order item resource .
     *
     * @param theProductOrderItem the product order item
     * @return response
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Response<ProductOrderItemEntity> createProductOrderItem(@RequestBody ProductOrderItemEntity theProductOrderItem) {
        return productOrderItemService.createProductOrderItem(theProductOrderItem);
    }

    /**
     * Find product order item by id.
     *
     * @param id the id
     * @return response
     */
    @PostMapping("/search/criteria/id")
    @ResponseStatus(HttpStatus.OK)
    public Response<ProductOrderItemEntity> findProductOrderItemById(@RequestBody String id) {
        return productOrderItemQueryService.findProductOrderItemById(UUID.fromString(id), EEntityLifeCycle.ACTIVE);
    }

    /**
     * Find Product Order Item By Purchase Order
     * @param thePurchaseOrder the PurchaseOrder
     * @return response
     */
    @PostMapping("/search/criteria/purchaseOrder")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<ProductOrderItemEntity>> findProductOrderItemByPurchaseOrder(@RequestBody PurchaseOrderEntity thePurchaseOrder) {
        return productOrderItemQueryService.findAllByPurchaseOrder(thePurchaseOrder);
    }

    /**
     * Find All product order items
     *
     * @return response
     */
    @PostMapping("/search/criteria/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<ProductOrderItemEntity>> findAllProductOrderItems() {
        return productOrderItemQueryService.findAllProductOrderItems();
    }

    /**
     * Update product order item resource .
     * @param theProductOrderItem the product order item
     * @return response
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Response<ProductOrderItemEntity> updateProductOrderItem(@RequestBody ProductOrderItemEntity theProductOrderItem) {
        return productOrderItemService.updateProductOrderItem(theProductOrderItem);
    }
}
