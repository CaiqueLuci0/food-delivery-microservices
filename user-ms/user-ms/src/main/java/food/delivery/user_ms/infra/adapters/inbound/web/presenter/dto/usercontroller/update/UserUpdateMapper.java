package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update;

import food.delivery.user_ms.core.domain.entities.User;

public final class UserUpdateMapper {

    private UserUpdateMapper() {
    }

    public static User toUser(UserUpdateRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        return user;
    }
}
