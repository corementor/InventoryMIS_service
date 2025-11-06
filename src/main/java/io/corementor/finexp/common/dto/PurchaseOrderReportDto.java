package io.corementor.finexp.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
@Getter
@Setter
public class PurchaseOrderReportDto {
    private BigDecimal totalPurchaseOrders = BigDecimal.ZERO;
    private BigDecimal totalCreated = BigDecimal.ZERO;
    private BigDecimal totalApproved = BigDecimal.ZERO;
    private BigDecimal totalSubmitted = BigDecimal.ZERO;
    private BigDecimal totalReturned = BigDecimal.ZERO;
}
