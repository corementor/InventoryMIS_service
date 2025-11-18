package io.corementor.finexp.rest.security;

import io.corementor.finexp.common.dto.RoleDto;
import io.corementor.finexp.core.security.service.RoleQueryService;
import io.corementor.finexp.orchestrator.security.RoleEntityOrchestratorQueryProcessor;
import io.corementor.finexp.core.security.domain.RoleEntity;
import io.corementor.finexp.core.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import psychemesh.framework.core.response.Response;

import java.util.List;

/**
 * The Class Role resource .
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/role")
public class RoleResource {
    /**
     * The role Service
     */
    private final RoleService roleService;
    /**
     * The role query service
     */
    private final RoleQueryService roleQueryService;
    /**
     * The role orchestrator query processor
     */
    private final RoleEntityOrchestratorQueryProcessor roleEntityOrchestratorQueryProcessor;

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

    /**
     * Find all roles
     *
     * @return response
     */
    /*@GetMapping("/search/criteria/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<RoleEntity>> findAllRoles() {
        return
                roleQueryServiceProcessor.findAllRoles();
    }*/
    @GetMapping("/search/criteria/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<RoleDto>> getAllActiveRoles() {
        return roleEntityOrchestratorQueryProcessor.listAllRoles();
    }
}
