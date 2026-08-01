package food.delivery.user_ms.infra.adapters.inbound.web.service;

import food.delivery.user_ms.core.application.ports.in.UserCrudUseCaseInputPort;
import food.delivery.user_ms.core.domain.entities.Address;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.user_ms.core.domain.exceptions.UnauthorizedException;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update.UserUpdateMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update.UserUpdateRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserCrudUseCaseInputPort userCrudUseCase;

    public UserService(UserCrudUseCaseInputPort userCrudUseCase) {
        this.userCrudUseCase = userCrudUseCase;
    }

    public UserResponseDto create(UserCreateRequestDto request) {
        User user = UserCreateMapper.toUser(request);
        Address address = UserCreateMapper.toAddress(request.getAddress());
        User created = userCrudUseCase.create(user, address);
        return UserResponseMapper.toResponse(created);
    }

    public UserResponseDto findById(UUID id) {
        return UserResponseMapper.toResponse(userCrudUseCase.findById(id));
    }

    public List<UserResponseDto> findAll() {
        return UserResponseMapper.toResponseList(userCrudUseCase.findAll());
    }

    public UserResponseDto update(UUID id, UserUpdateRequestDto request) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        User updated = userCrudUseCase.update(authenticatedUserId, id, UserUpdateMapper.toUser(request));
        return UserResponseMapper.toResponse(updated);
    }

    public void delete(UUID id) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        userCrudUseCase.delete(authenticatedUserId, id);
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UUID authenticatedUserId)) {
            throw new UnauthorizedException(ConstMessagesEnum.INVALID_CREDENTIALS.getMessage());
        }
        return authenticatedUserId;
    }
}
