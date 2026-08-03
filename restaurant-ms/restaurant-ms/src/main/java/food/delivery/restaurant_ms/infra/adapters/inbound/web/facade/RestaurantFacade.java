package food.delivery.restaurant_ms.infra.adapters.inbound.web.facade;

import food.delivery.restaurant_ms.core.application.ports.in.RestaurantCrudUseCaseInputPort;
import food.delivery.restaurant_ms.core.application.ports.out.ObjectStorageOutputPort;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;
import food.delivery.restaurant_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.restaurant_ms.core.domain.exceptions.ConflictException;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get.RestaurantResponseMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateMapper;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update.RestaurantUpdateRequestDto;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.security.AuthenticatedUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class RestaurantFacade {

    private final RestaurantCrudUseCaseInputPort restaurantCrudUseCase;
    private final ObjectStorageOutputPort objectStorageOutputPort;

    public RestaurantFacade(
            RestaurantCrudUseCaseInputPort restaurantCrudUseCase,
            ObjectStorageOutputPort objectStorageOutputPort
    ) {
        this.restaurantCrudUseCase = restaurantCrudUseCase;
        this.objectStorageOutputPort = objectStorageOutputPort;
    }

    @Transactional
    public RestaurantResponseDto create(RestaurantCreateRequestDto request) {
        Restaurant created = restaurantCrudUseCase.create(
                AuthenticatedUser.requireId(),
                RestaurantCreateMapper.toRestaurant(request),
                RestaurantCreateMapper.toAddress(request.getAddress())
        );
        return toResponse(created);
    }

    public RestaurantResponseDto findById(UUID id) {
        return toResponse(restaurantCrudUseCase.findById(id));
    }

    public List<RestaurantResponseDto> findAll(String search, BigDecimal latitude, BigDecimal longitude) {
        return restaurantCrudUseCase.findAll(search, latitude, longitude).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public RestaurantResponseDto update(UUID id, RestaurantUpdateRequestDto request) {
        Restaurant updated = restaurantCrudUseCase.update(
                AuthenticatedUser.requireId(),
                id,
                RestaurantUpdateMapper.toRestaurant(request),
                RestaurantUpdateMapper.toAddress(request)
        );
        return toResponse(updated);
    }

    @Transactional
    public RestaurantResponseDto uploadImage(UUID id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
        try {
            Restaurant updated = restaurantCrudUseCase.uploadImage(
                    AuthenticatedUser.requireId(),
                    id,
                    file.getInputStream(),
                    file.getSize(),
                    file.getContentType()
            );
            return toResponse(updated);
        } catch (IOException e) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
    }

    @Transactional
    public RestaurantResponseDto deleteImage(UUID id) {
        return toResponse(restaurantCrudUseCase.deleteImage(AuthenticatedUser.requireId(), id));
    }

    @Transactional
    public void delete(UUID id) {
        restaurantCrudUseCase.delete(AuthenticatedUser.requireId(), id);
    }

    private RestaurantResponseDto toResponse(Restaurant restaurant) {
        RestaurantResponseDto dto = RestaurantResponseMapper.toResponse(restaurant);
        if (dto != null) {
            dto.setImageUrl(objectStorageOutputPort.publicUrl(restaurant.getImageKey()));
        }
        return dto;
    }
}
