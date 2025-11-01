package io.corementor.finexp.core.inventory.productType.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import psychemesh.framework.domain.AbstractBaseEntity;

import java.math.BigDecimal;


/**
 * The Class ProductTypeEntity.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Entity
@Getter
@Setter
@Table(name = "product_type", schema = "inventory")
public class ProductTypeEntity extends AbstractBaseEntity {

    /**
     * The product code.
     */
    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    /**
     * The productName.
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * The description.
     */
    @Column(name = "description", nullable = false)
    private String description;
    /**
     * The size
     */
    @Column(name = "size", nullable = false)
    private int size;
    /**
     * The unitPrice.
     */
    @Column(name = "unit_price", nullable = true)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    /**
     * The sellUnitPrice.
     */
    @Column(name = "sell_unit_price", nullable = true)
    private BigDecimal sellUnitPrice = BigDecimal.ZERO;
}
