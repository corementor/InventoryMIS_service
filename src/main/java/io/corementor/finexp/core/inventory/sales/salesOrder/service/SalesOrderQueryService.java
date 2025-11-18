package io.corementor.finexp.core.inventory.sales.salesOrder.service;


import io.corementor.finexp.common.EOrderHistoryStatus;
import io.corementor.finexp.common.dto.SalesOrderReportDto;
import io.corementor.finexp.core.inventory.sales.salesOrder.domain.SalesOrderEntity;
import io.corementor.finexp.core.inventory.sales.salesOrder.repository.ISalesOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.math.BigDecimal;
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
    private final ISalesOrderRepository salesOrderRepository;

    /**
     * Find sales order by id
     *
     * @param id the id
     * @return response
     */
    public Response<SalesOrderEntity> findSalesOrderById(UUID id) {
        SalesOrderEntity salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(IUserMessage.INFORMATION_NOT_FOUND, "Purchase order object not found"));
        return new Response<>(salesOrder, IUserMessage.INFORMATION_FOUND);
    }

    /**
     * find all  sales orders
     * @return response
     */
    public Response<List<SalesOrderEntity>> findAllSalesOrders() {
        List<SalesOrderEntity> purchaseOrderList = salesOrderRepository
                .findAllActiveOrdersWithActiveItems(EEntityLifeCycle.ACTIVE);

        return new Response<>(purchaseOrderList, IUserMessage.INFORMATION_FOUND);
    }


    /**
     * generate report
     *
     * @return response <Sales order report dto>
     */
    public Response<SalesOrderReportDto> generateReport() {

        SalesOrderReportDto salesOrderReportDto = new SalesOrderReportDto();

        salesOrderReportDto.setTotalSalesOrders(BigDecimal.valueOf(this.salesOrderRepository.countAllByState(EEntityLifeCycle.ACTIVE)));
        salesOrderReportDto.setTotalCreated(BigDecimal.valueOf(this.salesOrderRepository.countAllByStatusAndState(EOrderHistoryStatus.CREATED, EEntityLifeCycle.ACTIVE)));
        salesOrderReportDto.setTotalSubmitted(BigDecimal.valueOf(this.salesOrderRepository.countAllByStatusAndState(EOrderHistoryStatus.SUBMITTED, EEntityLifeCycle.ACTIVE)));
        salesOrderReportDto.setTotalApproved(BigDecimal.valueOf(this.salesOrderRepository.countAllByStatusAndState(EOrderHistoryStatus.APPROVED, EEntityLifeCycle.ACTIVE)));
        salesOrderReportDto.setTotalReturned(BigDecimal.valueOf(this.salesOrderRepository.countAllByStatusAndState(EOrderHistoryStatus.RETURNED, EEntityLifeCycle.ACTIVE)));

        return new Response<>(salesOrderReportDto, IUserMessage.INFORMATION_FOUND);

    }
}
