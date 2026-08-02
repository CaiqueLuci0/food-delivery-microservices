package food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.create;

import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;

public final class RestaurantCreateMapper {

    private RestaurantCreateMapper() {
    }

    public static Restaurant toRestaurant(RestaurantCreateRequestDto request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        return restaurant;
    }

    public static Address toAddress(AddressRequestDto request) {
        if (request == null) {
            return null;
        }
        Address address = new Address();
        address.setCep(request.getCep());
        address.setLogradouro(request.getLogradouro());
        address.setNumero(request.getNumero());
        address.setComplemento(request.getComplemento());
        address.setBairro(request.getBairro());
        address.setCidade(request.getCidade());
        address.setUf(request.getUf());
        address.setReferencia(request.getReferencia());
        return address;
    }
}
