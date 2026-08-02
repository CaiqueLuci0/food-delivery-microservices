package food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.update;

import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;
import food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create.RestaurantCreateMapper;

public final class RestaurantUpdateMapper {

    private RestaurantUpdateMapper() {
    }

    public static Restaurant toRestaurant(RestaurantUpdateRequestDto request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        return restaurant;
    }

    public static Address toAddress(RestaurantUpdateRequestDto request) {
        return RestaurantCreateMapper.toAddress(request.getAddress());
    }
}
