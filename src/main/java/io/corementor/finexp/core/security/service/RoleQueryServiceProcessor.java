package io.corementor.finexp.core.security.service;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.core.security.domain.RoleEntity;
import io.corementor.finexp.core.security.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.message.IUserMessage;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Role query service class.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RoleQueryServiceProcessor {
    /**
     * The role repository
     */
    private final IRoleRepository repository;

    /**
     * find role by name and state
     *
     * @param roleName the String
     * @return Response<RoleEntity>
     */
    public Response<RoleEntity> findByRoleNameAndState(String roleName) {
        Optional<RoleEntity> found = repository.findAllByRoleNameAndState(roleName, EEntityLifeCycle.ACTIVE);
        if (found.isEmpty()) {
            return new Response<>(IMessage.INFORMATION_NOT_FOUND);
        } else {
            RoleEntity roleEntity = found.get();
            return new Response<>(roleEntity, IMessage.INFORMATION_FOUND);
        }
    }

    public Response<RoleEntity>findRoleById(UUID roleId){
        Optional<RoleEntity>found=repository.findRoleEntityByIdAndState(roleId,EEntityLifeCycle.ACTIVE);
        if(found.isPresent()){
            RoleEntity roleEntity=found.get();
            return new Response<>(roleEntity,IMessage.INFORMATION_FOUND);
        }else{
            return new Response<>(IMessage.INFORMATION_NOT_FOUND);
        }
    }


    /**
     * Fina all roles
     *
     * @return response
     */
    public Response<List<RoleEntity>> findAllRoles() {

        List<RoleEntity> roleEntities =
                repository.findAllByState(EEntityLifeCycle.ACTIVE);

        return
                new Response<>(roleEntities, roleEntities.isEmpty() ? IUserMessage.INFORMATION_NOT_FOUND : IUserMessage.INFORMATION_FOUND);
    }


}
