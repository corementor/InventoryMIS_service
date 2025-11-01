package io.corementor.finexp.transformers;

import io.corementor.finexp.common.UserDto;
import io.corementor.finexp.core.security.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.transformer.AbstractEntityTransformer;

/**
 * The Class user shallow transformer
 * @author BLAISE MUGISHA
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class UserShallowTransformer   extends AbstractEntityTransformer<UserDto, UserEntity> {

    /**
     * To entity.
     *
     * @param dto the dto
     * @return the entity
     */
    @Override
    public UserEntity transform(UserDto dto){
        return super.transform(dto);
    }


    /**
     * To dto.
     *
     * @param entity the entity
     * @return the dto
     */
    @Override
    public UserDto transform(UserEntity entity) {
        return super.transform(entity);
    }
}
