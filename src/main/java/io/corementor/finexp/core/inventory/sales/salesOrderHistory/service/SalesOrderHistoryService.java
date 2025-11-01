package io.corementor.finexp.core.inventory.sales.salesOrderHistory.service;


import io.corementor.finexp.core.inventory.sales.salesOrder.domain.SalesOrderEntity;
import io.corementor.finexp.core.inventory.sales.salesOrder.service.SalesOrderQueryService;
import io.corementor.finexp.core.inventory.sales.salesOrderHistory.domain.SalesOrderHistoryEntity;
import io.corementor.finexp.core.inventory.sales.salesOrderHistory.repository.ISalesOrderHistoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * The Class Sales Order History Service.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SalesOrderHistoryService {
    /**
     * The purchase order history repository
     */
    private final ISalesOrderHistoryRepo salesOrderHistoryRepo;

    /**
     * The purchase order Query service
     */
    private final SalesOrderQueryService salesOrderQueryService;

    /**
     * create purchase order history
     *
     * @param theSalesOrder the Sales order
     */
    public void createSalesOrderHistory(SalesOrderEntity theSalesOrder, String comment) {
        if (theSalesOrder == null) {
            log.warn("Sales order is null, cannot create history");
            return;
        }
        SalesOrderEntity purchaseOrder = salesOrderQueryService
                .findSalesOrderById(theSalesOrder.getId()).getData();

        if (purchaseOrder == null) {
            log.error("Sales order not found for ID: {}", theSalesOrder);
            return;
        }

        SalesOrderHistoryEntity history = new SalesOrderHistoryEntity();
        history.setSalesOrder(purchaseOrder);
        history.setStatus(purchaseOrder.getStatus());
        history.setCreatedAt(LocalDateTime.now());
        history.setCreatedBy(purchaseOrder.getCreatedBy());
        history.setComment(comment);

        salesOrderHistoryRepo.save(history);
        log.info("Created history for PO {} with status: {}",
                purchaseOrder.getSaleCode(), purchaseOrder.getStatus());
    }

}
