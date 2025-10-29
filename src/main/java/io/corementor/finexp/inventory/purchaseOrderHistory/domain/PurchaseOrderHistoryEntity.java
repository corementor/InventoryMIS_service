package io.corementor.finexp.inventory.purchaseOrderHistory.domain;


import io.corementor.finexp.common.EPurchaseOrderHistoryStatus;
import io.corementor.finexp.inventory.purchaseOrder.domain.PurchaseOrderEntity;
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
@Table(name = "inv_purchase_order_history", schema = "inventory")
public class PurchaseOrderHistoryEntity extends AbstractBaseEntity {
    /**
     * The purchase order
     */
    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrderEntity purchaseOrder;

    /**
     * The order history status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_history_status", nullable = false)
    private EPurchaseOrderHistoryStatus status;

    /**
     * The comment
     */
    @Column(name = "comments",length = 1000)
    private String comment;

}
