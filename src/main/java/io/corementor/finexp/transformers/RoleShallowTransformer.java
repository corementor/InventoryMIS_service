package io.corementor.finexp.transformers;

import io.corementor.finexp.common.RoleDto;
import io.corementor.finexp.core.security.domain.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.transformer.AbstractEntityTransformer;

/**
 * The Class Role Shallow Transformer.
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class RoleShallowTransformer  extends AbstractEntityTransformer<RoleDto, RoleEntity> {
    /**
     * To entity.
     *
     * @param dto the dto
     * @return the supplier entity
     */
    @Override
    public RoleEntity transform(RoleDto dto){
        return super.transform(dto);
    }

    /**
     * To dto.
     *
     * @param entity the entity
     * @return the supplier dto
     */
    @Override
    public RoleDto transform(RoleEntity entity) {
        return super.transform(entity);
    }
}
