package io.corementor.finexp.orchestrator.security;

import io.corementor.finexp.common.dto.UserDto;
import io.corementor.finexp.core.security.service.UserService;
import io.corementor.finexp.objectTransformer.security.UserDeepTransformer;
import io.corementor.finexp.objectTransformer.security.UserShallowTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.core.response.Response;
import psychemesh.framework.core.util.HashIdUtility;
/**
 * The Class User Entity Orchestrator Processor
 * @author BLAISE MUGISHA
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class UserEntityOrchestratorProcessor {
    /**
     * The User service
     */
    private final UserService userService;
    /**
     * The User shallow transformer
     */
    private final UserShallowTransformer userShallowTransformer;
    /**
     * The User deep transformer
     */
    private final UserDeepTransformer userDeepTransformer;
    /**
     * The Hash Id utility
     */
    private final HashIdUtility hashIdUtility;

    /**
     * create user entity
     * @param theUserDto  the user dto
     * @return response UserDto
     */
    public Response<UserDto> createUserEntity(UserDto theUserDto) {

        return userShallowTransformer.transform(
                userService.createUser(
                        userDeepTransformer.transform(theUserDto)
                )
        );

    }
    /**
     * Update user entity
     * @param theUserDto  the user dto
     * @return response UserDto
     */
    public Response<UserDto> updateUserEntity(UserDto theUserDto) {
        return userShallowTransformer.transform(
                userService.updateUser(
                        userDeepTransformer.transform(theUserDto)
                )
        );

    }

    public Response<UserDto> deleteUser(String id) {
        return userShallowTransformer.transform(
                userService.deleteUser(
                       hashIdUtility.decode(id)
                )
        );
    }
}
