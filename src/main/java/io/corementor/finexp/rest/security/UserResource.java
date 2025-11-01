package io.corementor.finexp.rest.security;

import io.corementor.finexp.base.IMessage;
import io.corementor.finexp.common.UserDto;
import io.corementor.finexp.orchestrator.UserEntityOrchestratorProcessor;
import io.corementor.finexp.orchestrator.UserEntityOrchestratorQueryProcessor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

import java.util.List;
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
     * The user orchestrator query service processor
     */
    private final UserEntityOrchestratorQueryProcessor userEntityOrchestratorQueryProcessor;
    /**
     * The user orchestrator query service processor
     */
    private final UserEntityOrchestratorProcessor userEntityOrchestratorProcessor;

    /**
     * Create user.
     *
     * @param userDto the user dto
     * @return response
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Response<UserDto> createUser(@RequestBody UserDto userDto) {
        return userEntityOrchestratorProcessor.createUserEntity(userDto);
    }

    /**
     * Create user.
     *
     * @param userDto the user dto
     * @return response
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Response<UserDto> updateUser(@RequestBody UserDto userDto) {
        return userEntityOrchestratorProcessor.updateUserEntity(userDto);
    }

    /**
     * Delete user
     *
     * @param id the string
     * @return response
     */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<UserDto> deleteUser(@PathVariable("id") String id,
                                        HttpServletRequest request
    ) {
        try {

            System.out.println("CONTROLLER REACHED");
            String body = request.getReader().lines()
                    .collect(Collectors.joining());
            System.out.println("Raw body: " + body);
            return userEntityOrchestratorProcessor.deleteUser(id);
        } catch (Exception e) {
            return new Response<>(IMessage.INFORMATION_NOT_FOUND);
        }
    }

    /**
     * List all users
     *
     * @return response
     */
    @GetMapping("/search/criteria/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<UserDto>> listUsers() {
        return userEntityOrchestratorQueryProcessor.getAllActiveUsers();
    }

    /**
     * Find user by id
     *
     * @param id the user id
     * @return response
     */
    @GetMapping("/search/criteria/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<UserDto> findUserById(@PathVariable String id) {
        return userEntityOrchestratorQueryProcessor.getUserEntityById(id);
    }
}
