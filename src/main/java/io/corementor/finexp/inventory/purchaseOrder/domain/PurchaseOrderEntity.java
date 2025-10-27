package io.corementor.finexp.inventory.purchaseOrder.domain;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import psychemesh.framework.domain.AbstractBaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class PurchaseOrderEntity.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Getter@Setter
@Entity
@Table(name = "inv_purchase_order", schema = "inventory")
public class PurchaseOrderEntity extends AbstractBaseEntity {
    /**
     * The purchase code.
     */
    @Column(name = "purchase_code", nullable = false, unique = true)
    private String purchaseCode;
    /**
     * The purchase date.
     */
    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;
    /**
     * The total price.
     */
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "purchaseOrderEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductOrderItemEntity> orderItems = new ArrayList<>();
}
