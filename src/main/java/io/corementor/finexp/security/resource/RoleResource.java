package io.corementor.finexp.security.resource;

import io.corementor.finexp.security.domain.RoleEntity;
import io.corementor.finexp.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

/**
 * The Role resource class.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/role")
public class RoleResource {
    /**
     * The roleService
     */
    private final RoleService roleService;

    /**
     * Create role.
     *
     * @param roleEntity the role entity
     * @return response
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Response<RoleEntity> createRole(@RequestBody String roleEntity) {
        return roleService.createRole(roleEntity);
    }
}
