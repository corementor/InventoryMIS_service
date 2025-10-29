package io.corementor.finexp.security.service;

import io.corementor.finexp.common.AuthResponse;
import io.corementor.finexp.common.LoginDto;
import io.corementor.finexp.common.RefreshTokenRequest;
import io.corementor.finexp.security.config.JwtService;
import io.corementor.finexp.security.config.MyUserDetailService;
import io.corementor.finexp.security.domain.RoleEntity;
import io.corementor.finexp.security.domain.UserEntity;
import io.corementor.finexp.security.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * The Authentication service class.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Service@RequiredArgsConstructor
public class AuthenticationService {
    /*** The IUserRepository Interface */
    private final IUserRepository userRepository;
    /*** The Jwt service   */
    private final JwtService jwtService;
    /*** The Authentication Manager */
    private final AuthenticationManager authenticationManager;
    /*** The User Details Service */
    private final MyUserDetailService userDetailsService;

    /**
     * authenticate
     *
     * @param request LoginDto
     * @return AuthResponse
     */
    @Transactional(readOnly = true)
    public AuthResponse authenticate(LoginDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );



            UserEntity user = userRepository.findUserEntityByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            System.out.println("=== DEBUG USER DATA ===");
            System.out.println("User: " + user.getEmail());
            System.out.println("Roles Count: " + user.getRole().size());
            System.out.println("Roles: " +
                    user.getRole().stream()
                            .map(RoleEntity::getRoleName)
                            .collect(Collectors.joining(", ")));
            System.out.println("=== END DEBUG ===");
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            String roles = user.getRole().stream()
                    .map(RoleEntity::getRoleName)
                    .collect(Collectors.joining(","));
            String fullNames = user.getFirstName() + " " + user.getLastName();

            return AuthResponse.builder()
                    .email(user.getEmail())
                    .fullName(fullNames)
                    .role(roles)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)

                    .message("Authentication successful")
                    .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    /**
     * refresh Token
     *
     * @param request LoginDto
     * @return AuthResponse
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                UserEntity user = (UserEntity) userDetails;
                String accessToken = jwtService.generateToken(user);

                return AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .email(user.getEmail())
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .role(user.getRole().stream()
                                .map(RoleEntity::getRoleName)
                                .collect(Collectors.joining(",")))
                        .message("Token refreshed successfully")
                        .build();
            }
        }
        throw new RuntimeException("Invalid refresh token");
    }
}
