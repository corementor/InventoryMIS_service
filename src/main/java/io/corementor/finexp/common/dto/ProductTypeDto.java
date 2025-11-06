package io.corementor.finexp.common.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import psychemesh.framework.common.dto.AbstractBaseDto;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductTypeDto extends AbstractBaseDto {
    /**
     * The product code.
     */
    private String productCode;

    /**
     * The productName.
     */
    private String productName;

    /**
     * The description.
     */
    private String description;
    /**
     * The size
     */
    private int size;
    /**
     * The unitPrice.
     */
    private BigDecimal unitPrice = BigDecimal.ZERO;

    /**
     * The sellUnitPrice.
     */
    private BigDecimal sellUnitPrice = BigDecimal.ZERO;
}
