package food.delivery.order_ms.infra.adapters.inbound.websocket;

import food.delivery.order_ms.core.application.ports.out.TokenOutputPort;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class JwtStompChannelInterceptor implements ChannelInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenOutputPort tokenOutputPort;

    public JwtStompChannelInterceptor(TokenOutputPort tokenOutputPort) {
        this.tokenOutputPort = tokenOutputPort;
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() != StompCommand.CONNECT) {
            return message;
        }

        String token = extractToken(accessor);
        if (token == null) {
            throw new IllegalArgumentException("Missing authentication token");
        }

        UUID userId = tokenOutputPort.extractUserId(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid authentication token"));

        accessor.setUser(new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList()));
        return message;
    }

    private String extractToken(StompHeaderAccessor accessor) {
        String authorization = firstHeader(accessor, "Authorization");
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length()).trim();
        }
        String token = firstHeader(accessor, "token");
        if (token != null && !token.isBlank()) {
            return token.trim();
        }
        return null;
    }

    private static String firstHeader(StompHeaderAccessor accessor, String name) {
        List<String> values = accessor.getNativeHeader(name);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }
}
