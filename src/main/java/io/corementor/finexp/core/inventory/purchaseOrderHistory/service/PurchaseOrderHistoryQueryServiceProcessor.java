package io.corementor.finexp.core.inventory.purchaseOrderHistory.service;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.core.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.core.inventory.purchaseOrder.service.PurchaseOrderQueryService;
import io.corementor.finexp.core.inventory.purchaseOrderHistory.domain.PurchaseOrderHistoryEntity;
import io.corementor.finexp.core.inventory.purchaseOrderHistory.repository.IPurchaseOrderHistoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class Purchase Order History Service.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderHistoryQueryServiceProcessor {

    /**
     * The purchase order history repo
     */
    private final IPurchaseOrderHistoryRepo purchaseOrderHistoryRepo;

    /**
     * The purchase order query service
     */
    private final PurchaseOrderQueryService purchaseOrderQueryService;

    /**
     * Get Purchase order history
     *
     * @param purchaseOrderId the purchase order
     * @return List
     */


    public Response<List<PurchaseOrderHistoryEntity>> getPurchaseOrderHistory(UUID purchaseOrderId) {
        try {

            Response<PurchaseOrderEntity> purchaseOrderResponse =
                    purchaseOrderQueryService.findPurchaseOrderById(purchaseOrderId);

            if (purchaseOrderResponse.getData() == null) {
                return new Response<>(IUserMessage.INFORMATION_NOT_FOUND);
            }


            List<PurchaseOrderHistoryEntity> history =
                    purchaseOrderHistoryRepo.findByPurchaseOrderIdOrderByCreatedAtDesc(purchaseOrderId);

            if (history.isEmpty()) {
                return new Response<>("No history found for this purchase order");
            }

            return new Response<>(history, IMessage.INFORMATION_FOUND);

        } catch (Exception e) {
            log.error("Error retrieving history for purchase order {}: {}", purchaseOrderId, e.getMessage());
            return new Response<>(IUserMessage.ERROR);
        }
    }
}
