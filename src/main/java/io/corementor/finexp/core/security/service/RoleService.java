package io.corementor.finexp.core.security.service;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.core.security.domain.RoleEntity;
import io.corementor.finexp.core.security.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.core.response.Response;

import java.time.LocalDateTime;

/**
 * The Role service class.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Service@RequiredArgsConstructor
public class RoleService {
    /**
     * The role repository.
     */
    private final IRoleRepository roleRepository;

    /**
     * Create role.
     *
     * @param  roleName the String
     * @return response
     */
    public Response<RoleEntity> createRole(String roleName) {
        if (roleName == null) {
            return new Response<>(IMessage.INVALID_INPUT);
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName(roleName);
        roleEntity.setCreatedAt(LocalDateTime.now());
        return new Response<>(roleRepository.save(roleEntity));
    }
}
