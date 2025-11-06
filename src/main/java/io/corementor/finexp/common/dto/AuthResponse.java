package io.corementor.finexp.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The AuthResponse class.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String email;
    private String fullName;
    private String role;
    private String accessToken;
    private String refreshToken;
    private String message;

}
