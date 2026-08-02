package food.delivery.restaurant_ms.infra.adapters.inbound.web.service;

import food.delivery.restaurant_ms.core.application.ports.in.RestaurantCrudUseCaseInputPort;
import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;
import food.delivery.restaurant_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.restaurant_ms.core.domain.exceptions.UnauthorizedException;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantCrudUseCaseInputPort restaurantCrudUseCase;

    public RestaurantService(RestaurantCrudUseCaseInputPort restaurantCrudUseCase) {
        this.restaurantCrudUseCase = restaurantCrudUseCase;
    }

    @Transactional
    public RestaurantResponseDto create(RestaurantCreateRequestDto request) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        Restaurant restaurant = RestaurantCreateMapper.toRestaurant(request);
        Address address = RestaurantCreateMapper.toAddress(request.getAddress());
        Restaurant created = restaurantCrudUseCase.create(authenticatedUserId, restaurant, address);
        return RestaurantResponseMapper.toResponse(created);
    }

    public RestaurantResponseDto findById(UUID id) {
        return RestaurantResponseMapper.toResponse(restaurantCrudUseCase.findById(id));
    }

    public List<RestaurantResponseDto> findAll(String search) {
        return RestaurantResponseMapper.toResponseList(restaurantCrudUseCase.findAll(search));
    }

    @Transactional
    public RestaurantResponseDto update(UUID id, RestaurantUpdateRequestDto request) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        Restaurant updated = restaurantCrudUseCase.update(
                authenticatedUserId,
                id,
                RestaurantUpdateMapper.toRestaurant(request),
                RestaurantUpdateMapper.toAddress(request)
        );
        return RestaurantResponseMapper.toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        restaurantCrudUseCase.delete(authenticatedUserId, id);
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UUID authenticatedUserId)) {
            throw new UnauthorizedException(ConstMessagesEnum.INVALID_CREDENTIALS.getMessage());
        }
        return authenticatedUserId;
    }
}
