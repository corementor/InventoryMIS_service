package io.corementor.finexp.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DashboardReportDto {
    private PurchaseOrderReportDto purchaseOrderReport;
    private SalesOrderReportDto salesOrderReportDto;
    private int totalProductTypes;
    private int totalUsers;
}
