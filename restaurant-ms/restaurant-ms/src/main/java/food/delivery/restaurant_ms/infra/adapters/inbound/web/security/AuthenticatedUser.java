package food.delivery.restaurant_ms.infra.adapters.inbound.web.security;

import food.delivery.restaurant_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.restaurant_ms.core.domain.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class AuthenticatedUser {

    private AuthenticatedUser() {
    }

    public static UUID requireId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UUID authenticatedUserId)) {
            throw new UnauthorizedException(ConstMessagesEnum.INVALID_CREDENTIALS.getMessage());
        }
        return authenticatedUserId;
    }
}
