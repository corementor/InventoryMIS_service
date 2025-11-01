package io.corementor.finexp.orchestrator;

import io.corementor.finexp.common.RoleDto;
import io.corementor.finexp.core.security.service.RoleQueryServiceProcessor;
import io.corementor.finexp.transformers.RoleShallowTransformer;
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
    private final RoleQueryServiceProcessor roleQueryServiceProcessor;

    /**
     * List all roles.
     *
     * @return Response
     */
    public Response<List<RoleDto>> listAllRoles() {
        return roleShallowTransformer.transformList(
                roleQueryServiceProcessor.findAllRoles()
        );
    }
}
