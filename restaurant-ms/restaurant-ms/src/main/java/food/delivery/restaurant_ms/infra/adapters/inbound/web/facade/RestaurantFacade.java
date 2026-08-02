package food.delivery.restaurant_ms.infra.adapters.inbound.web.facade;

import food.delivery.restaurant_ms.core.application.ports.in.RestaurantCrudUseCaseInputPort;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.security.AuthenticatedUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class RestaurantFacade {

    private final RestaurantCrudUseCaseInputPort restaurantCrudUseCase;

    public RestaurantFacade(RestaurantCrudUseCaseInputPort restaurantCrudUseCase) {
        this.restaurantCrudUseCase = restaurantCrudUseCase;
    }

    @Transactional
    public RestaurantResponseDto create(RestaurantCreateRequestDto request) {
        Restaurant created = restaurantCrudUseCase.create(
                AuthenticatedUser.requireId(),
                RestaurantCreateMapper.toRestaurant(request),
                RestaurantCreateMapper.toAddress(request.getAddress())
        );
        return RestaurantResponseMapper.toResponse(created);
    }

    public RestaurantResponseDto findById(UUID id) {
        return RestaurantResponseMapper.toResponse(restaurantCrudUseCase.findById(id));
    }

    public List<RestaurantResponseDto> findAll(String search, BigDecimal latitude, BigDecimal longitude) {
        return RestaurantResponseMapper.toResponseList(
                restaurantCrudUseCase.findAll(search, latitude, longitude)
        );
    }

    @Transactional
    public RestaurantResponseDto update(UUID id, RestaurantUpdateRequestDto request) {
        Restaurant updated = restaurantCrudUseCase.update(
                AuthenticatedUser.requireId(),
                id,
                RestaurantUpdateMapper.toRestaurant(request),
                RestaurantUpdateMapper.toAddress(request)
        );
        return RestaurantResponseMapper.toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        restaurantCrudUseCase.delete(AuthenticatedUser.requireId(), id);
    }
}
