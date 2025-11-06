package io.corementor.finexp.rest.report;

import io.corementor.finexp.common.dto.DashboardReportDto;
import io.corementor.finexp.orchestrator.inventory.ProductTypeOrchestratorQueryProcessor;
import io.corementor.finexp.orchestrator.security.UserEntityOrchestratorQueryProcessor;
import io.corementor.finexp.orchestrator.inventory.PurchaseOrderOrchestratorQueryProcessor;
import io.corementor.finexp.orchestrator.inventory.SalesOrderOrchestratorQueryProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

/**
 * The Class DashboardReport Resource
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardReportResource {
    /**
     * The purchase order orchestrator query processor
     */
    private final PurchaseOrderOrchestratorQueryProcessor purchaseOrderOrchestratorQueryProcessor;
    /**
     * The sales order  orchestrator query processor
     */
    private final SalesOrderOrchestratorQueryProcessor salesOrderOrchestratorQueryProcessor;
    /**
     * The user  orchestrator query processor
     */
    private final UserEntityOrchestratorQueryProcessor userEntityOrchestratorQueryProcessor;
    /**
     * The product type orchestrator processor
     */
    private final ProductTypeOrchestratorQueryProcessor productTypeOrchestratorQueryProcessor;

    /**
     * generate report
     *
     * @return response <Dashboard dto>
     */
    @GetMapping("/report")
    public Response<DashboardReportDto> generateReport() {
        DashboardReportDto reportDto = new DashboardReportDto();
        reportDto.setPurchaseOrderReport(this.purchaseOrderOrchestratorQueryProcessor.generateReport().getData());
        reportDto.setSalesOrderReportDto(this.salesOrderOrchestratorQueryProcessor.generateReport().getData());
        reportDto.setTotalUsers(this.userEntityOrchestratorQueryProcessor.countActiveUsers());
        reportDto.setTotalProductTypes(this.productTypeOrchestratorQueryProcessor.countProductTypes());
//        System.out.println(reportDto);
        return new Response<>(reportDto, IUserMessage.INFORMATION_FOUND);
    }
}
