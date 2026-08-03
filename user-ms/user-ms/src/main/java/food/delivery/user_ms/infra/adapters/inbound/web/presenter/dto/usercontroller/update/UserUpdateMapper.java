package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update;

import food.delivery.user_ms.core.domain.entities.Address;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateMapper;

public final class UserUpdateMapper {

    private UserUpdateMapper() {
    }

    public static User toUser(UserUpdateRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        return user;
    }

    public static Address toAddress(UserUpdateRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return UserCreateMapper.toAddress(dto.getAddress());
    }
}
