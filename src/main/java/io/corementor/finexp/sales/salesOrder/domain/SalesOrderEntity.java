package io.corementor.finexp.sales.salesOrder.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.corementor.finexp.sales.saleOrderItem.domain.SalesOrderItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import psychemesh.framework.domain.AbstractBaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The class SalesOrderEntity.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Getter
@Setter
@Entity
@Table(name = "inv_sales_order" , schema = "inventory")
public class SalesOrderEntity  extends AbstractBaseEntity {
    /**
     * The sale code.
     */
    @Column(name = "sale_code", nullable = false, unique = true)
    private String saleCode;
    /**
     * The sale date.
     */
    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;
    /**
     * The total price.
     */
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "saleOrderEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SalesOrderItemEntity> orderItems = new ArrayList<>();
}
