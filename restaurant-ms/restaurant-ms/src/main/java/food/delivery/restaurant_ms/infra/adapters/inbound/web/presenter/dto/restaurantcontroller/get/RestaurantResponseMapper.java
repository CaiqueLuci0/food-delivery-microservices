package food.delivery.restaurant_ms.infra.adapters.inbound.web.presenter.dto.restaurantcontroller.get;

import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;

import java.util.List;

public final class RestaurantResponseMapper {

    private RestaurantResponseMapper() {
    }

    public static RestaurantResponseDto toResponse(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setOwnerId(restaurant.getOwnerId());
        dto.setImageKey(restaurant.getImageKey());
        dto.setAddress(toAddressResponse(restaurant.getAddress()));
        return dto;
    }

    public static List<RestaurantResponseDto> toResponseList(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantResponseMapper::toResponse).toList();
    }

    private static AddressResponseDto toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }
        AddressResponseDto dto = new AddressResponseDto();
        dto.setId(address.getId());
        dto.setCep(address.getCep());
        dto.setLogradouro(address.getLogradouro());
        dto.setNumero(address.getNumero());
        dto.setComplemento(address.getComplemento());
        dto.setBairro(address.getBairro());
        dto.setCidade(address.getCidade());
        dto.setUf(address.getUf());
        dto.setReferencia(address.getReferencia());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());
        return dto;
    }
}
