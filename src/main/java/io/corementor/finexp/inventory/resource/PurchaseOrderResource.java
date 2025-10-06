package io.corementor.finexp.inventory.resource;

import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.inventory.purchaseOrder.service.PurchaseOrderQueryService;
import io.corementor.finexp.inventory.purchaseOrder.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

import java.util.List;

/**
 * The Class PurchaseOrderResource.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@CrossOrigin("*")
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
     * Find all purchase order
     *
     * @return response
     */
    @GetMapping("/search/criteria/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<PurchaseOrderEntity>> findAllPurchaseOrders() {
        return purchaseOrderQueryService.findAllPurchaseOrders();
    }

}
