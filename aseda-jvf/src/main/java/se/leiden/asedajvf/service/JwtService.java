package se.leiden.asedajvf.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.leiden.asedajvf.enums.Role;
import se.leiden.asedajvf.exeptions.UnauthorizedException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long validityInSeconds;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long validityInSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.validityInSeconds = validityInSeconds;
    }

    public String createToken(String username, int memberId, Role role) {
        Instant now = Instant.now();
        Instant validity = now.plus(validityInSeconds, ChronoUnit.SECONDS);

        return Jwts.builder()
                .subject(username)
                .claim("memberId", memberId)
                .claim("role", role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(validity))
                .signWith(secretKey)
                .compact();
    }

    public Claims validateTokenAndGetClaims(String token) throws UnauthorizedException {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Token has expired");
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    public String extractToken(String authorizationHeader) throws UnauthorizedException {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new UnauthorizedException("Invalid authorization header");
    }
}
