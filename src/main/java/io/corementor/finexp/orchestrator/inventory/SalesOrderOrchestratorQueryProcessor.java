package io.corementor.finexp.orchestrator.inventory;

import io.corementor.finexp.common.dto.SalesOrderReportDto;
import io.corementor.finexp.core.inventory.sales.salesOrder.service.SalesOrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

/**
 * The Class Sales Order Orchestrator Query Processor
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class SalesOrderOrchestratorQueryProcessor {
    /**
     * The sales order query processor
     */
    private  final SalesOrderQueryService salesOrderQueryService;

    /**
     * generate report
     *
     * @return response <SalesOrderReport>
     */

    public Response<SalesOrderReportDto> generateReport() {
        SalesOrderReportDto salesOrderReportDto = salesOrderQueryService.generateReport().getData();
        return new Response<>(salesOrderReportDto, IUserMessage.INFORMATION_FOUND);
    }


}
