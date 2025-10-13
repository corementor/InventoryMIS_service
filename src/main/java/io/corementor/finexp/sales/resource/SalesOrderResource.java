package io.corementor.finexp.sales.resource;

import io.corementor.finexp.sales.salesOrder.domain.SalesOrderEntity;
import io.corementor.finexp.sales.salesOrder.service.SalesOrderQueryService;
import io.corementor.finexp.sales.salesOrder.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

import java.util.List;

/**
 * The Class Sales Order Resource.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@CrossOrigin("*")
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
     * @param theSalesOrder the sales order
     * @return response
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Response<SalesOrderEntity> updateSalesOrder(@RequestBody SalesOrderEntity theSalesOrder) {
        return salesOrderService.updateSalesOrderWithItems(theSalesOrder);
    }
}
