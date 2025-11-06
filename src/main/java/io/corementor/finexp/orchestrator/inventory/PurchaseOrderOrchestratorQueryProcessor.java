package io.corementor.finexp.orchestrator.inventory;

import io.corementor.finexp.common.dto.PurchaseOrderReportDto;
import io.corementor.finexp.core.inventory.purchaseOrder.service.PurchaseOrderQueryService;
import io.corementor.finexp.core.inventory.purchaseOrder.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.core.response.Response;

/**
 * The Class Purchase Order Orchestrator Processor
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class PurchaseOrderOrchestratorQueryProcessor {

    /**
     * The purchase order query service
     */
    private final PurchaseOrderQueryService purchaseOrderQueryService;

    /**
     * generate report
     * @return response <PurchaseOrderDto>
     */
    public Response<PurchaseOrderReportDto> generateReport(){
        return purchaseOrderQueryService.generateReport();
    }


}
