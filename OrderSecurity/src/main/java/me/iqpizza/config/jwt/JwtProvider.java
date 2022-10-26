package me.iqpizza.config.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import me.iqpizza.config.security.dto.AuthenticateUser;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private static final String IDENT_ACCESS = "ACCESS";
    private static final String IDENT_REFRESH = "REFRESH";
    private final JwtProperties jwtProperties;
    private final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    public String generateAccessToken(AuthenticateUser authenticateUser) {
        return generateToken(IDENT_ACCESS, authenticateUser.getId(),
                authenticateUser.getRole(), jwtProperties.getExpirationSecond());
    }

    public String generateRefreshToken(AuthenticateUser authenticateUser) {
        return generateToken(IDENT_REFRESH, authenticateUser.getId(),
                authenticateUser.getRole(), jwtProperties.getExpirationSecond() * 168);
    }

    private String generateToken(String type, long id, String role, long expSecond) {
        final Date tokenCreationDate = new Date();
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("type", type);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(id))
                .setIssuedAt(tokenCreationDate)
                .setExpiration(new Date(tokenCreationDate.getTime() + expSecond))
                .signWith(generateKey(type), algorithm)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Base64.decodeBase64(jwtProperties.getSecret()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }
    }

    public String extractSubjectFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public Jws<Claims> extractRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Base64.decodeBase64(jwtProperties.getSecret()))
                .build()
                .parseClaimsJws(token);
    }

    public String getTokenFromHeader(String headerValue) {
        if (headerValue != null && headerValue.startsWith("Bearer ")) {
            return headerValue.replace("Bearer ", "");
        }

        return null;
    }

    public AuthenticateUser parseAuthenticateUser(String token) {
        final long id = Long.parseLong(extractSubjectFromToken(token));
        Jws<Claims> claimsJws = extractRoleFromToken(token);
        final String role = (String) claimsJws.getBody().get("role");
        return new AuthenticateUser(id, role);
    }

    public long getExpirationSecond() {
        return jwtProperties.getExpirationSecond();
    }

    private Key generateKey(String type) {
        String secretKey;
        if (type.equals(IDENT_ACCESS)) {
            secretKey = jwtProperties.getSecret();
        }
        else {
            secretKey = jwtProperties.getRefresh();
        }

        byte[] apiKeySecret = Base64.decodeBase64(secretKey);
        return new SecretKeySpec(apiKeySecret, algorithm.getJcaName());
    }
}
