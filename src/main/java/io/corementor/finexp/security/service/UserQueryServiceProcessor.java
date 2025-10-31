package io.corementor.finexp.security.service;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.security.domain.UserEntity;
import io.corementor.finexp.security.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.response.Response;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The User Query service class.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserQueryServiceProcessor {
    /**
     * The User repository service
     */
    private final IUserRepository userRepository;

    /**
     * List all users
     *
     * @return response
     */
    public Response<List<UserEntity>> listUsers() {
        return new Response<>(userRepository.findAllByState(EEntityLifeCycle.ACTIVE));
    }

    /**
     * Find by id
     *
     * @return response
     */
    public Response<UserEntity> findUserById(UUID id) {
        Optional<UserEntity> optionalUser = userRepository.findByIdAndState(id, EEntityLifeCycle.ACTIVE);
        return optionalUser.map(userEntity -> new Response<>(userEntity, IMessage.INFORMATION_FOUND)).orElseGet(() -> new Response<>(IMessage.INFORMATION_NOT_FOUND));

    }

    /**
     * Find user by entity
     *
     * @return response
     */
    public Response<UserEntity> findUserByEntity(UserEntity theUser) {
        Optional<UserEntity> optionalUser = userRepository.findUserEntityByIdAndState(theUser.getId(), EEntityLifeCycle.ACTIVE);
        return optionalUser.map(userEntity -> new Response<>(userEntity, IMessage.INFORMATION_FOUND)).orElseGet(() -> new Response<>(IMessage.INFORMATION_NOT_FOUND));
    }


    /**
     * Get All Users
     *
     * @param pageable the Pageable
     * @return page
     */

    public Page<UserEntity> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
