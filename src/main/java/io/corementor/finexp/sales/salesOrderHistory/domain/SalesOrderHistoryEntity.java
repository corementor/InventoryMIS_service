package io.corementor.finexp.sales.salesOrderHistory.domain;


import io.corementor.finexp.common.EOrderHistoryStatus;
import io.corementor.finexp.sales.salesOrder.domain.SalesOrderEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import psychemesh.framework.domain.AbstractBaseEntity;

/**
 * The Class Purchase Order History Entity.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "inv_sales_order_history", schema = "inventory")
public class SalesOrderHistoryEntity extends AbstractBaseEntity {
    /**
     * The sales order
     */
    @ManyToOne
    @JoinColumn(name = "sales_order_id", nullable = false)
    private SalesOrderEntity salesOrder;

    /**
     * The order history status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_history_status", nullable = false)
    private EOrderHistoryStatus status;

    /**
     * The comment
     */
    @Column(name = "comments",length = 1000)
    private String comment;

}
