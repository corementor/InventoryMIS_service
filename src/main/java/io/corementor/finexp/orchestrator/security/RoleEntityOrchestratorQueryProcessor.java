package io.corementor.finexp.orchestrator.security;

import io.corementor.finexp.common.dto.RoleDto;
import io.corementor.finexp.core.security.service.RoleQueryService;
import io.corementor.finexp.objectTransformer.security.RoleShallowTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.core.response.Response;

import java.util.List;

/**
 * The Class Role Entity Orchestrator Query Processor
 * @author BLAISE MUGISHA
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class RoleEntityOrchestratorQueryProcessor {
    /**
     * The role shallow transformer
     */
    private final RoleShallowTransformer roleShallowTransformer;
    /**
     * The role shallow query transformer
     */
    private final RoleQueryService roleQueryService;

    /**
     * List all roles.
     *
     * @return Response
     */
    public Response<List<RoleDto>> listAllRoles() {
        return roleShallowTransformer.transformList(
                roleQueryService.findAllRoles()
        );
    }
}
