package io.corementor.finexp.sales.salesOrder.service;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
import io.corementor.finexp.sales.salesOrder.domain.SalesOrderEntity;
import io.corementor.finexp.sales.salesOrder.repository.ISalesOrderRepository;
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
 * The Class Sales Order Query Service.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SalesOrderQueryService {
    /**
     * The sales order repository.
     *
     */
    private final ISalesOrderRepository saleOrderRepository;

    /**
     * Find sales order by id
     *
     * @param id the id
     * @return response
     */
    public Response<SalesOrderEntity> findSalesOrderById(UUID id) {
        SalesOrderEntity salesOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(IMessage.INFORMATION_NOT_FOUND, "Purchase order object not found"));
        return new Response<>(salesOrder, IMessage.INFORMATION_FOUND);
    }

    /**
     * find all  sales orders
     * @return response
     */
    public Response<List<SalesOrderEntity>> findAllSalesOrders() {
        List<SalesOrderEntity> purchaseOrderList = saleOrderRepository
                .findAllByState(EEntityLifeCycle.ACTIVE);

        return new Response<>(purchaseOrderList, IUserMessage.INFORMATION_FOUND);
    }


}
