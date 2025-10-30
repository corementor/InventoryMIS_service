package io.corementor.finexp.sales.salesOrderHistory.service;

import io.corementor.finexp.base.IMessage;

import io.corementor.finexp.inventory.purchaseOrderHistory.repository.IPurchaseOrderHistoryRepo;
import io.corementor.finexp.sales.salesOrder.domain.SalesOrderEntity;
import io.corementor.finexp.sales.salesOrder.service.SalesOrderQueryService;
import io.corementor.finexp.sales.salesOrderHistory.domain.SalesOrderHistoryEntity;
import io.corementor.finexp.sales.salesOrderHistory.repository.ISalesOrderHistoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class Sales Order History Service.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SalesOrderHistoryQueryServiceProcessor {

    /**
     * The sales order history repo
     */
    private final ISalesOrderHistoryRepo salesOrderHistoryRepo;

    /**
     * The sales order query service
     */
    private final SalesOrderQueryService salesOrderQueryService;

    /**
     * Get Sales order history
     *
     * @param salesOrderId the sales order
     * @return List
     */


    public Response<List<SalesOrderHistoryEntity>> getSalesOrderHistory(UUID salesOrderId) {
        try {

            Response<SalesOrderEntity> salesOrderResponse =
                    salesOrderQueryService.findSalesOrderById(salesOrderId);

            if (salesOrderResponse.getData() == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }


            List<SalesOrderHistoryEntity> history =
                    salesOrderHistoryRepo.findBySalesOrderIdOrderByCreatedAtDesc(salesOrderId);

            if (history.isEmpty()) {
                return new Response<>("No history found for this sales order");
            }

            return new Response<>(history, IMessage.INFORMATION_FOUND);

        } catch (Exception e) {
            log.error("Error retrieving history for sales order {}: {}", salesOrderId, e.getMessage());
            return new Response<>(IUserMessage.ERROR);
        }
    }
}
