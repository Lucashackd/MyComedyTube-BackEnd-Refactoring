// Responsabilidade: Validar regras e gerenciar a transação. Usamos EJB (@Stateless).

package br.com.mycomedytube.service;

import br.com.mycomedytube.model.User;
import br.com.mycomedytube.model.UserToken;
import br.com.mycomedytube.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import javax.crypto.SecretKey;
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
        this.key = Jwts.SIG.HS256.key().build();
    }

    public String generateToken(User user) {
        String tokenString = Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key)
                .compact();

        UserToken userToken = new UserToken(tokenString, user);
        repository.save(userToken);

        return tokenString;
    }

    public String validateToken(String tokenString) {
        try {
            String email = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(tokenString)
                    .getPayload()
                    .getSubject();

            Optional<UserToken> tokenStored = repository.searchByString(tokenString);

            if (tokenStored.isEmpty()) return null;

            return email;
        } catch (Exception e) {
            return null;
        }
    }

    public void invalidadeToken (String tokenString) {
        repository.deleteByString(tokenString);
    }
}
