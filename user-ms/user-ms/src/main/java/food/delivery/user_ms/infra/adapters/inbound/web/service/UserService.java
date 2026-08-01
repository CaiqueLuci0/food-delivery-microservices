package food.delivery.user_ms.infra.adapters.inbound.web.service;

import food.delivery.user_ms.core.application.ports.in.UserCrudUseCaseInputPort;
import food.delivery.user_ms.core.domain.entities.Adress;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateRequestDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.UserCreateResponseDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseDto;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get.UserResponseMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update.UserUpdateMapper;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.update.UserUpdateRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserCrudUseCaseInputPort userCrudUseCase;

    public UserService(UserCrudUseCaseInputPort userCrudUseCase) {
        this.userCrudUseCase = userCrudUseCase;
    }

    public UserCreateResponseDto create(UserCreateRequestDto request) {
        User user = UserCreateMapper.toUser(request);
        Adress adress = UserCreateMapper.toAdress(request.getAdress());
        User created = userCrudUseCase.create(user, adress);
        return UserCreateMapper.toResponse(created);
    }

    public UserResponseDto findById(UUID id) {
        return UserResponseMapper.toResponse(userCrudUseCase.findById(id));
    }

    public List<UserResponseDto> findAll() {
        return UserResponseMapper.toResponseList(userCrudUseCase.findAll());
    }

    public UserResponseDto update(UUID id, UserUpdateRequestDto request) {
        User updated = userCrudUseCase.update(id, UserUpdateMapper.toUser(request));
        return UserResponseMapper.toResponse(updated);
    }

    public void delete(UUID id) {
        userCrudUseCase.delete(id);
    }
}
