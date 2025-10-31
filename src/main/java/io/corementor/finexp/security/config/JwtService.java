package io.corementor.finexp.security.config;

import io.corementor.finexp.security.domain.RoleEntity;
import io.corementor.finexp.security.domain.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Jwt service class
 * @author Blaise Mugisha
 * @version 1.0
 */
@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.expiration-time}")
    private long EXPIRATION_TIME;

    @Value("${security.jwt.refresh-expiration-time}")
    private long REFRESH_EXPIRATION_TIME;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserEntity user) {
        return buildToken(user, EXPIRATION_TIME);
    }

    public String generateRefreshToken(UserEntity user) {
        return buildToken(user, REFRESH_EXPIRATION_TIME);
    }

    private String buildToken(UserEntity user, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRole().stream()
                .map(RoleEntity::getRoleName)
                .collect(Collectors.toSet()));

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

      /*  System.out.println("Secret key length: " + SECRET_KEY.length());
        System.out.println("Decoded key bytes length: " + keyBytes.length);*/

        return Keys.hmacShaKeyFor(keyBytes);
    }
}