package io.corementor.finexp.inventory.purchaseOrder.service;


import io.corementor.finexp.inventory.base.IMessage;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.inventory.purchaseOrder.repository.IPurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * The Class PurchaseOrderQueryService.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderQueryService {

    /**
     * The purchase order repository.
     */
    private final IPurchaseOrderRepository purchaseOrderRepository;

    /**
     * Find purchase order by id
     *
     * @param id the id
     * @return response
     */
    public Response<PurchaseOrderEntity> findPurchaseOrderById(UUID id) {
        PurchaseOrderEntity purchaseOrderEntity = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(IMessage.INFORMATION_NOT_FOUND, "Purchase order object not found"));
        return new Response<>(purchaseOrderEntity, IMessage.INFORMATION_FOUND);
    }

    /**
     * find all  purchase orders
     * @return response
     */
    public Response<List<PurchaseOrderEntity>> findAllPurchaseOrders() {
        List<PurchaseOrderEntity> purchaseOrderList = purchaseOrderRepository
                .findAllByState(EEntityLifeCycle.ACTIVE);

        return new Response<>(purchaseOrderList, IUserMessage.INFORMATION_FOUND);
    }
}
