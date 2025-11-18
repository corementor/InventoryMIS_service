package io.corementor.finexp.objectTransformer.inventory;

import io.corementor.finexp.common.dto.ProductTypeDto;
import io.corementor.finexp.core.inventory.productType.domain.ProductTypeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.transformer.AbstractEntityTransformer;

/**
 * The Class ProductType shallow transformer
 * @author BLAISE MUGISHA
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class ProductTypeShallowTransformer extends AbstractEntityTransformer<ProductTypeDto, ProductTypeEntity> {

    /**
     * To entity.
     *
     * @param dto the dto
     * @return the entity
     */
    @Override
    public ProductTypeEntity transform(ProductTypeDto dto){
        return super.transform(dto);
    }

    /**
     * To dto.
     *
     * @param entity the entity
     * @return the dto
     */
    @Override
    public ProductTypeDto transform(ProductTypeEntity entity){
        return super.transform(entity);
    }
}
