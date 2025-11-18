package io.corementor.finexp.rest.inventory;


import io.corementor.finexp.core.inventory.sales.salesOrderHistory.domain.SalesOrderHistoryEntity;
import io.corementor.finexp.core.inventory.sales.salesOrderHistory.service.SalesOrderHistoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class Sales Order History Resource.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/salesOrder/history")
@RequiredArgsConstructor
public class SalesOrderHistoryResource {

    /***
     * The sales order history query service processor
     */

    private final SalesOrderHistoryQueryService salesOrderHistoryQueryService;

    /**
     * Get Sales order history
     *
     * @param salesOrderId the UUID
     * @return response
     */

    @GetMapping("/{salesOrderId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<SalesOrderHistoryEntity>> getSalesOrderHistory(
            @PathVariable UUID salesOrderId) {
        return salesOrderHistoryQueryService.getSalesOrderHistory(salesOrderId);
    }
}
