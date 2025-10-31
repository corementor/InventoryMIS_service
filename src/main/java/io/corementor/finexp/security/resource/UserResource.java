package io.corementor.finexp.security.resource;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.security.domain.UserEntity;
import io.corementor.finexp.security.service.UserQueryServiceProcessor;
import io.corementor.finexp.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The User resource class.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserResource {
    /**
     * The user service.
     */
    private final UserService userService;
    /**
     * The user query service
     */
    private final UserQueryServiceProcessor userQueryServiceProcessor;

    /**
     * Create user.
     *
     * @param userEntity the user entity
     * @return response
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Response<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        return userService.createUser(userEntity);
    }

    /**
     * Create user.
     *
     * @param userEntity the user entity
     * @return response
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Response<UserEntity> updateUser(@RequestBody UserEntity userEntity) {
        return userService.updateUser(userEntity);
    }

    /**
     * Delete user
     *
     * @param userEntity the user entity
     * @return response
     */
    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public Response<UserEntity> deleteUser(@RequestBody UserEntity userEntity,
                                           HttpServletRequest request
    ) {
        try {
            String body = request.getReader().lines()
                    .collect(Collectors.joining());
            System.out.println("Raw body: " + body);
            return userService.deleteUser(userEntity);
        } catch (Exception e) {
            return new Response<>(IMessage.INFORMATION_NOT_FOUND);
        }
//
    }

    /**
     * List all users
     *
     * @return response
     */
    @GetMapping("/search/criteria/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<UserEntity>> listUsers() {
        return userQueryServiceProcessor.listUsers();
    }

    /**
     * Find user by id
     *
     * @param id the user id
     * @return response
     */
    @GetMapping("/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<UserEntity> findUserById(@PathVariable UUID id) {
        return userQueryServiceProcessor.findUserById(id);
    }
}
