package io.corementor.finexp.security.resource;

import io.corementor.finexp.common.AuthResponse;
import io.corementor.finexp.common.LoginDto;
import io.corementor.finexp.common.RefreshTokenRequest;
import io.corementor.finexp.security.config.JwtService;
import io.corementor.finexp.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class Authentication Resource.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/security/auth")
public class AuthenticationResource {
    /*** The Authentication Orchestrator Processor*/
    private final AuthenticationService authenticationOrchestratorProcessor;
    /*** The Jwt service */
    private final JwtService jwtService;


    /**
     * login
     *
     * @param loginRequest loginRequest
     * @return responseEntity
     */

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginRequest) {
        try {
            AuthResponse response = authenticationOrchestratorProcessor.authenticate(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, null, null, "Invalid username or password"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse(null, null, null, null, null, "User not found"));
        } catch (Exception ex) {
            ex.printStackTrace(); // Log the full error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(null, null, null, null, null, "Authentication failed: " + ex.getMessage()));
        }
    }

    /**
     * refresh token
     *
     * @param request request
     * @return AuthResponse
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse response = authenticationOrchestratorProcessor.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, null, null, "Token refresh failed"));
        }
    }


}
