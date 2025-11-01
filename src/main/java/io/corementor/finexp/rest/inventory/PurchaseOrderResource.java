package io.corementor.finexp.rest.inventory;

import io.corementor.finexp.common.RequestDto;
import io.corementor.finexp.core.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.core.inventory.purchaseOrder.service.PurchaseOrderQueryService;
import io.corementor.finexp.core.inventory.purchaseOrder.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class Purchase Order Resource.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/purchaseOrder")
public class PurchaseOrderResource {
    /**
     * The purchaseOrderService
     */
    private final PurchaseOrderService purchaseOrderService;

    /**
     * The purchaseOrderQueryService
     */
    private final PurchaseOrderQueryService purchaseOrderQueryService;


    /**
     * Create purchase order
     *
     * @param purchaseOrderEntity the purchase order entity
     * @return response
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Response<PurchaseOrderEntity> createPurchaseOrder(@RequestBody PurchaseOrderEntity purchaseOrderEntity) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderEntity);
    }

    /**
     * Update purchase order
     *
     * @param thePurchaseOrder the purchase order
     * @return response
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Response<PurchaseOrderEntity> updatePurchaseOrder(@RequestBody PurchaseOrderEntity thePurchaseOrder) {
        return purchaseOrderService.updatePurchaseOrderWithItems(thePurchaseOrder);
    }

    /**
     * Find all purchase order
     *
     * @return response
     */
    @GetMapping("/search/criteria/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<PurchaseOrderEntity>> findAllPurchaseOrders() {
        return purchaseOrderQueryService.findAllPurchaseOrders();
    }

    /**
     * Delete purchase order
     *
     * @param thePurchaseOrder the purchase order entity
     * @return response
     */
    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public Response<PurchaseOrderEntity> deletePurchaseOrder(@RequestBody PurchaseOrderEntity thePurchaseOrder) {
        return purchaseOrderService.deletePurchaseOrder(thePurchaseOrder);
    }

    /**
     * Delete purchase order item
     *
     * @param itemId the item ID
     * @return response
     */
    @DeleteMapping("/delete/item/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<PurchaseOrderEntity> deletePurchaseOrderItem(@PathVariable("itemId") UUID itemId) {
        try {
            return purchaseOrderService.deletePurchaseOrderItem(itemId);
        } catch (Exception e) {
            return new Response<>(IUserMessage.ERROR);
        }
    }

    /**
     * Get purchase order by id
     *
     * @param theId The UUId
     * @return response
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<PurchaseOrderEntity> getPurchaseOrderById(@PathVariable("id") UUID theId) {
        return purchaseOrderQueryService.findPurchaseOrderById(theId);
    }


    /**
     * Submit for approval
     *
     * @param requestDto the RequestDto
     * @return response
     */
    @PostMapping("/submitForApproval")
    @ResponseStatus(HttpStatus.OK)
    public Response<PurchaseOrderEntity> submitForApproval(@RequestBody RequestDto requestDto) {
        return purchaseOrderService.submitForApproval(requestDto);
    }

    /**
     * Approve Purchase Order
     *
     * @param requestDto the RequestDto
     * @return response
     */
    @PostMapping("/approveOrder")
    @ResponseStatus(HttpStatus.OK)
    public Response<PurchaseOrderEntity> approvePurchaseOrder(@RequestBody RequestDto requestDto) {
        return purchaseOrderService.approvePurchaseOrder(requestDto);
    }

    /**
     * Return Purchase Order
     *
     * @param requestDto the RequestDto
     * @return response
     */
    @PostMapping("/returnOrder")
    @ResponseStatus(HttpStatus.OK)
    public Response<PurchaseOrderEntity> returnPurchaseOrder(@RequestBody RequestDto requestDto) {
        return purchaseOrderService.returnPurchaseOrder(requestDto);
    }


}
