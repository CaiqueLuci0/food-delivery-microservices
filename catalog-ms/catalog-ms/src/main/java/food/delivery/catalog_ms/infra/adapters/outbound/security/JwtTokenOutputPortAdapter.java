package food.delivery.catalog_ms.infra.adapters.outbound.security;

import food.delivery.catalog_ms.core.application.ports.out.TokenOutputPort;
import food.delivery.catalog_ms.infra.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtTokenOutputPortAdapter implements TokenOutputPort {

    private final SecretKey secretKey;

    public JwtTokenOutputPortAdapter(JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Optional<UUID> extractUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(UUID.fromString(claims.getSubject()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
