package io.corementor.finexp.sales.resource;


import io.corementor.finexp.sales.salesOrderHistory.domain.SalesOrderHistoryEntity;
import io.corementor.finexp.sales.salesOrderHistory.service.SalesOrderHistoryQueryServiceProcessor;
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

    private final SalesOrderHistoryQueryServiceProcessor salesOrderHistoryQueryServiceProcessor;

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
        return salesOrderHistoryQueryServiceProcessor.getSalesOrderHistory(salesOrderId);
    }
}
