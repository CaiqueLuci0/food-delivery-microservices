package food.delivery.restaurant_ms.infra.adapters.outbound.persistence.mappers;

import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;
import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jpaentities.JpaAddress;
import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jpaentities.JpaRestaurant;

public final class RestaurantPersistenceMapper {

    private RestaurantPersistenceMapper() {
    }

    public static JpaRestaurant toJpa(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        JpaRestaurant jpaRestaurant = new JpaRestaurant();
        jpaRestaurant.setId(restaurant.getId());
        jpaRestaurant.setName(restaurant.getName());
        jpaRestaurant.setDescription(restaurant.getDescription());
        jpaRestaurant.setOwnerId(restaurant.getOwnerId());
        jpaRestaurant.setImageKey(restaurant.getImageKey());
        if (restaurant.getAddress() != null) {
            JpaAddress jpaAddress = AddressPersistenceMapper.toJpa(restaurant.getAddress());
            jpaRestaurant.setAddress(jpaAddress);
        }
        return jpaRestaurant;
    }

    public static Restaurant toDomain(JpaRestaurant jpaRestaurant) {
        if (jpaRestaurant == null) {
            return null;
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setId(jpaRestaurant.getId());
        restaurant.setName(jpaRestaurant.getName());
        restaurant.setDescription(jpaRestaurant.getDescription());
        restaurant.setOwnerId(jpaRestaurant.getOwnerId());
        restaurant.setImageKey(jpaRestaurant.getImageKey());
        Address address = AddressPersistenceMapper.toDomain(jpaRestaurant.getAddress());
        if (address != null) {
            address.setRestaurant(restaurant);
            restaurant.setAddress(address);
        }
        return restaurant;
    }
}
