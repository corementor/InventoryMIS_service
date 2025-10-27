package io.corementor.finexp.inventory.purchaseOrder.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.corementor.finexp.inventory.productType.domain.ProductTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import psychemesh.framework.domain.AbstractBaseEntity;

import java.math.BigDecimal;

/**
 * The Class Product Order ItemEntity.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Getter@Setter
@Entity
@Table(name = "product_order_item", schema = "inventory")
public class ProductOrderItemEntity extends AbstractBaseEntity {
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
     * The productName.
     */
    @Column(name = "product_name", nullable = true)
    private String productName;
    /**
     * The size.
     */
    @Column(name = "size", nullable = true)
    private int size;
    /**
     * The tax rate.
     */
    @Column(name = "tax_amount",nullable = true)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    /**
     * The totalTax.
     */
    @Column(name = "total_tax", nullable = true)
    private BigDecimal totalTax = BigDecimal.ZERO;
    /**
     * The totalPriceWithTax.
     */
    @Column(name = "total_price_with_tax",nullable = true)
    private BigDecimal totalPriceWithTax = BigDecimal.ZERO;

    /**
     * The purchase order.
     */
    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    @JsonBackReference
    private PurchaseOrderEntity purchaseOrderEntity;
    /**
     * The product type.
     */
    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductTypeEntity productType;
}