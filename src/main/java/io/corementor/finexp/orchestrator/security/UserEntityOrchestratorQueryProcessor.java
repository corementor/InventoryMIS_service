package io.corementor.finexp.orchestrator.security;

import io.corementor.finexp.common.dto.UserDto;
import io.corementor.finexp.core.security.service.UserQueryServiceProcessor;
import io.corementor.finexp.transformers.security.UserDeepTransformer;
import io.corementor.finexp.transformers.security.UserShallowTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psychemesh.framework.core.response.Response;
import psychemesh.framework.core.util.HashIdUtility;

import java.util.List;
import java.util.UUID;
/**
 * The Class User Entity Orchestrator Query Processor
 * @author BLAISE MUGISHA
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserEntityOrchestratorQueryProcessor {
    private final UserQueryServiceProcessor userEntityQueryServiceProcessor;
    private final HashIdUtility hashIdUtility;
    private final UserDeepTransformer userDeepTransformer;
    private final UserShallowTransformer userShallowTransformer;

    /**
     * get user by id
     *
     * @param id UUID
     * @return response
     */
    public Response<UserDto> getUserEntityById(String id) {
        UUID decodedId = hashIdUtility.decode(id);
        return userDeepTransformer.transform(
                userEntityQueryServiceProcessor.findUserById(decodedId)
        );

    }


    /**
     * get all active users
     *
     * @return response
     */
    public Response<List<UserDto>> getAllActiveUsers() {
        return
                userShallowTransformer.transformList(
                        userEntityQueryServiceProcessor.listUsers()

                );

    }

    public int countActiveUsers(){
        return userEntityQueryServiceProcessor.countUsers();
    }
}
