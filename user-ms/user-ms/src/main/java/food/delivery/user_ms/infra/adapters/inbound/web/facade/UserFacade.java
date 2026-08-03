package food.delivery.user_ms.infra.adapters.inbound.web.facade;

import food.delivery.user_ms.core.application.ports.in.UserCrudUseCaseInputPort;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update.UserUpdateMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update.UserUpdateRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.security.AuthenticatedUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class UserFacade {

    private final UserCrudUseCaseInputPort userCrudUseCase;

    public UserFacade(UserCrudUseCaseInputPort userCrudUseCase) {
        this.userCrudUseCase = userCrudUseCase;
    }

    @Transactional
    public UserResponseDto create(UserCreateRequestDto request) {
        User created = userCrudUseCase.create(
                UserCreateMapper.toUser(request),
                UserCreateMapper.toAddress(request.getAddress())
        );
        return UserResponseMapper.toResponse(created);
    }

    public UserResponseDto findById(UUID id) {
        return UserResponseMapper.toResponse(userCrudUseCase.findById(id));
    }

    public List<UserResponseDto> findAll() {
        return UserResponseMapper.toResponseList(userCrudUseCase.findAll());
    }

    @Transactional
    public UserResponseDto update(UUID id, UserUpdateRequestDto request) {
        User updated = userCrudUseCase.update(
                AuthenticatedUser.requireId(),
                id,
                UserUpdateMapper.toUser(request),
                UserUpdateMapper.toAddress(request)
        );
        return UserResponseMapper.toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        userCrudUseCase.delete(AuthenticatedUser.requireId(), id);
    }
}
