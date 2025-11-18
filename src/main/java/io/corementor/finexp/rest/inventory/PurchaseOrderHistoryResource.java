package io.corementor.finexp.rest.inventory;

import io.corementor.finexp.core.inventory.purchaseOrderHistory.domain.PurchaseOrderHistoryEntity;
import io.corementor.finexp.core.inventory.purchaseOrderHistory.service.PurchaseOrderHistoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class Purchase Order History Resource.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/purchaseOrder/history")
@RequiredArgsConstructor
public class PurchaseOrderHistoryResource {

    /***
     * The purchase order history query service processor
     */

    private final PurchaseOrderHistoryQueryService purchaseOrderHistoryQueryService;

    /**
     * Get Purchase order history
     *
     * @param purchaseOrderId the UUID
     * @return response
     */

    @GetMapping("/{purchaseOrderId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<PurchaseOrderHistoryEntity>> getPurchaseOrderHistory(
            @PathVariable UUID purchaseOrderId) {
        return purchaseOrderHistoryQueryService.getPurchaseOrderHistory(purchaseOrderId);
    }
}
