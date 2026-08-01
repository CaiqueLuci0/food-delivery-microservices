package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.authcontroller.login;

import food.delivery.user_ms.core.domain.entities.UserLoginDetails;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseMapper;

public final class LoginMapper {

    private LoginMapper() {
    }

    public static LoginResponseDto toResponse(UserLoginDetails userLoginDetails) {
        return new LoginResponseDto(
                userLoginDetails.getToken(),
                UserResponseMapper.toResponse(userLoginDetails.getUser())
        );
    }
}
