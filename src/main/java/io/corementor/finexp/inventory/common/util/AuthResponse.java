package io.corementor.finexp.inventory.common.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
