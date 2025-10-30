package io.corementor.finexp.inventory.purchaseOrderHistory.service;

import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.inventory.purchaseOrder.service.PurchaseOrderQueryService;
import io.corementor.finexp.inventory.purchaseOrderHistory.domain.PurchaseOrderHistoryEntity;
import io.corementor.finexp.inventory.purchaseOrderHistory.repository.IPurchaseOrderHistoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * The Class Purchase Order History Service.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderHistoryService {
    /**
     * The purchase order history repository
     */
    private final IPurchaseOrderHistoryRepo purchaseOrderHistoryRepo;

    /**
     * The purchase order Query service
     */
    private final PurchaseOrderQueryService purchaseOrderQueryService;

    /**
     * create purchase order history
     *
     * @param thePurchaseOrder the Purchase order
     */
    public void createPurchaseOrderHistory(PurchaseOrderEntity thePurchaseOrder, String comment) {
        if (thePurchaseOrder == null) {
            log.warn("Purchase order is null, cannot create history");
            return;
        }
        PurchaseOrderEntity purchaseOrder = purchaseOrderQueryService
                .findPurchaseOrderById(thePurchaseOrder.getId()).getData();

        if (purchaseOrder == null) {
            log.error("Purchase order not found for ID: {}", thePurchaseOrder);
            return;
        }

        PurchaseOrderHistoryEntity history = new PurchaseOrderHistoryEntity();
        history.setPurchaseOrder(purchaseOrder);
        history.setStatus(purchaseOrder.getStatus());
        history.setCreatedAt(LocalDateTime.now());
        history.setCreatedBy(purchaseOrder.getCreatedBy());
        history.setComment(comment);

        purchaseOrderHistoryRepo.save(history);
        log.info("Created history for PO {} with status: {}",
                purchaseOrder.getPurchaseCode(), purchaseOrder.getStatus());
    }

}
