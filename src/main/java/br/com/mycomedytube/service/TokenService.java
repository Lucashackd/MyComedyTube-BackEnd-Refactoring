// Responsabilidade: Validar regras e gerenciar a transação. Usamos EJB (@Stateless).

package br.com.mycomedytube.service;

import br.com.mycomedytube.model.User;
import br.com.mycomedytube.model.UserToken;
import br.com.mycomedytube.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Stateless
public class TokenService {

    @Inject
    private TokenRepository repository;

    private SecretKey key;

    @PostConstruct
    public void init() {
        String secretString = System.getenv("JWT_SECRET");

        if (secretString == null || secretString.trim().isEmpty()) {
            throw new IllegalStateException("CRITICAL ERROR: environment variable 'JWT_SECRET' is not configured in the system");
        }

        if (secretString.length() < 32) {
            throw new IllegalStateException("CRITICAL ERROR: the key 'JWT_SECRET' must have at least 32 characters");
        }

        this.key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        String tokenString = Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key)
                .compact();

        UserToken userToken = new UserToken(tokenString, user);
        repository.save(userToken);

        return tokenString;
    }

    public Claims validateToken(String tokenString) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(tokenString)
                    .getPayload();

            Optional<UserToken> tokenStored = repository.searchByString(tokenString);

            if (tokenStored.isEmpty()) return null;

            return claims;
        } catch (Exception e) {
            return null;
        }
    }

    public void invalidadeToken(String tokenString) {
        repository.deleteByString(tokenString);
    }
}
