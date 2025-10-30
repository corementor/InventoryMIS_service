package io.corementor.finexp.sales.resource;

import io.corementor.finexp.common.RequestDto;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.sales.salesOrder.domain.SalesOrderEntity;
import io.corementor.finexp.sales.salesOrder.service.SalesOrderQueryService;
import io.corementor.finexp.sales.salesOrder.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class Sales Order Resource.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/salesOrder")
public class SalesOrderResource {

    /**
     * The sales Order Service
     */
    private final SalesOrderService salesOrderService;

    /**
     * The sales Order Query service
     */
    private final SalesOrderQueryService salesOrderQueryService;

    /**
     * Create sales order
     *
     * @param salesOrderEntity the sales order entity
     * @return response
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Response<SalesOrderEntity> createSalesOrder(@RequestBody SalesOrderEntity salesOrderEntity) {
        return salesOrderService.createSalesOrder(salesOrderEntity);
    }

    /**
     * Find all sales order
     *
     * @return response
     */
    @GetMapping("/search/criteria/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<SalesOrderEntity>> findAllSalesOrders() {
        return salesOrderQueryService.findAllSalesOrders();
    }

    /**
     * Update sales order
     *
     * @param theSalesOrder the sales order
     * @return response
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Response<SalesOrderEntity> updateSalesOrder(@RequestBody SalesOrderEntity theSalesOrder) {
        return salesOrderService.updateSalesOrderWithItems(theSalesOrder);
    }

    /**
     * Delete sales order item
     * @param itemId the UUID
     * @return response
     */
    @DeleteMapping("/delete/item/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<SalesOrderEntity> deleteSalesOrderItem(@PathVariable UUID itemId) {
        return salesOrderService.deleteSalesOrderItem(itemId);
    }

    /**
     * Submit for approval
     *
     * @param requestDto the RequestDto
     * @return response
     */
    @PostMapping("/submitForApproval")
    @ResponseStatus(HttpStatus.OK)
    public Response<SalesOrderEntity> submitForApproval(@RequestBody RequestDto requestDto) {
        return salesOrderService.submitForApproval(requestDto);
    }

    /**
     * Approve Sales Order
     *
     * @param requestDto the RequestDto
     * @return response
     */
    @PostMapping("/approveOrder")
    @ResponseStatus(HttpStatus.OK)
    public Response<SalesOrderEntity> approveSalesOrder(@RequestBody RequestDto requestDto) {
        return salesOrderService.approveSalesOrder(requestDto);
    }

    /**
     * Return Sales Order
     *
     * @param requestDto the RequestDto
     * @return response
     */
    @PostMapping("/returnOrder")
    @ResponseStatus(HttpStatus.OK)
    public Response<SalesOrderEntity> returnSalesOrder(@RequestBody RequestDto requestDto) {
        return salesOrderService.returnSalesOrder(requestDto);
    }

}
