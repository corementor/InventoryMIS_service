package io.corementor.finexp.sales.salesOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.corementor.finexp.inventory.productType.domain.ProductTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import psychemesh.framework.domain.AbstractBaseEntity;

import java.math.BigDecimal;

/**
 * The class Sale Order ItemEntity.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Getter
@Setter
@Entity
@Table(name = "sales_order_item", schema = "inventory")
public class SalesOrderItemEntity extends AbstractBaseEntity {
    /**
     * The product Name.
     */
    @Column(name = "product_name", nullable = true)
    private String productName;
    /**
     * The size.
     */
    @Column(name = "size", nullable = true)
    private int size;
    /**
     * The quantity.
     */
    @Column(name = "quantity", nullable = true)
    private int quantity;
    /**
     * The unit price.
     */
    @Column(name = "unit_price", nullable = true)
    private BigDecimal unitPrice;
    /**
     * Total price
     */
    @Column(name = "total_price", nullable = true)
    private BigDecimal totalPrice;
    /**
     * The sale order.
     */
    @ManyToOne
    @JoinColumn(name = "sale_order_id", nullable = false)
    @JsonBackReference
    private SalesOrderEntity saleOrderEntity;
    /**
     * The product type.
     */
    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductTypeEntity productType;
}
