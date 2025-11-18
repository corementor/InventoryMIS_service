package io.corementor.finexp.core.security.config;

import io.corementor.finexp.core.security.service.UserQueryService;
import io.corementor.finexp.core.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * The Initial Runner App Class.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialRunnerApp implements CommandLineRunner {
    /**
     * The User service
     */
    private final UserService userEntityService;
    /**
     * The User Query processor service
     */
    private final UserQueryService userEntityQueryServiceProcessor;

    @Override
    public void run(String... args) {
        if (userEntityQueryServiceProcessor.getAllUsers(PageRequest.of(0, 1)).isEmpty()) {
            userEntityService.createAdminUser();
        } else {
            log.info("Admin user already exists. Skipping creation.");
        }
    }
}
