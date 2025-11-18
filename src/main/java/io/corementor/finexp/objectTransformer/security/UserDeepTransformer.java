package io.corementor.finexp.objectTransformer.security;

import io.corementor.finexp.common.dto.UserDto;
import io.corementor.finexp.core.security.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.transformer.AbstractEntityTransformer;

import java.util.Objects;

/**
 * The Class User Deep Transformer.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class UserDeepTransformer extends AbstractEntityTransformer<UserDto, UserEntity> {
    /**
     * The Role shallow transformer.
     */
    private final RoleShallowTransformer roleShallowTransformer;



    @Override
    public UserEntity transform(UserDto source) {
        UserEntity entity = super.transform(source);
        if (Objects.nonNull(source) && Objects.nonNull(source.getRole()))
            entity.setRole(roleShallowTransformer.transformSet(source.getRole()));
        return entity;
    }

    @Override
    public UserDto transform(UserEntity source) {
        UserDto dto = super.transform(source);
        if (Objects.nonNull(source) && Objects.nonNull(source.getRole()))
            dto.setRole(roleShallowTransformer.transform(source.getRole()));
        return dto;


    }

}
