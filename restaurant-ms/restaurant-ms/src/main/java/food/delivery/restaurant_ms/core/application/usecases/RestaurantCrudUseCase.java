package food.delivery.restaurant_ms.core.application.usecases;

import food.delivery.restaurant_ms.core.application.ports.in.RestaurantCrudUseCaseInputPort;
import food.delivery.restaurant_ms.core.application.ports.out.CepLookupOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.GeocodingOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.ObjectStorageOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantCreatedEventOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantDeletedEventOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.RestaurantRepositoryOutputPort;
import food.delivery.restaurant_ms.core.application.ports.out.UserReferenceRepositoryOutputPort;
import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.core.domain.entities.CepAddress;
import food.delivery.restaurant_ms.core.domain.entities.GeoCoordinates;
import food.delivery.restaurant_ms.core.domain.entities.Restaurant;
import food.delivery.restaurant_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.restaurant_ms.core.domain.exceptions.ConflictException;
import food.delivery.restaurant_ms.core.domain.exceptions.ForbiddenException;
import food.delivery.restaurant_ms.core.domain.exceptions.NotFoundException;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RestaurantCrudUseCase implements RestaurantCrudUseCaseInputPort {

    private static final double SEARCH_RADIUS_METERS = 3000d;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private final RestaurantRepositoryOutputPort restaurantRepositoryOutputPort;
    private final UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort;
    private final CepLookupOutputPort cepLookupOutputPort;
    private final GeocodingOutputPort geocodingOutputPort;
    private final RestaurantCreatedEventOutputPort restaurantCreatedEventOutputPort;
    private final RestaurantDeletedEventOutputPort restaurantDeletedEventOutputPort;
    private final ObjectStorageOutputPort objectStorageOutputPort;

    public RestaurantCrudUseCase(
            RestaurantRepositoryOutputPort restaurantRepositoryOutputPort,
            UserReferenceRepositoryOutputPort userReferenceRepositoryOutputPort,
            CepLookupOutputPort cepLookupOutputPort,
            GeocodingOutputPort geocodingOutputPort,
            RestaurantCreatedEventOutputPort restaurantCreatedEventOutputPort,
            RestaurantDeletedEventOutputPort restaurantDeletedEventOutputPort,
            ObjectStorageOutputPort objectStorageOutputPort
    ) {
        this.restaurantRepositoryOutputPort = restaurantRepositoryOutputPort;
        this.userReferenceRepositoryOutputPort = userReferenceRepositoryOutputPort;
        this.cepLookupOutputPort = cepLookupOutputPort;
        this.geocodingOutputPort = geocodingOutputPort;
        this.restaurantCreatedEventOutputPort = restaurantCreatedEventOutputPort;
        this.restaurantDeletedEventOutputPort = restaurantDeletedEventOutputPort;
        this.objectStorageOutputPort = objectStorageOutputPort;
    }

    @Override
    public Restaurant findById(UUID id) {
        return restaurantRepositoryOutputPort.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage()));
    }

    @Override
    public List<Restaurant> findAll(String search, BigDecimal latitude, BigDecimal longitude) {
        boolean hasLatitude = latitude != null;
        boolean hasLongitude = longitude != null;
        if (hasLatitude != hasLongitude) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }

        String normalizedSearch = (search == null || search.isBlank()) ? null : search.trim();

        if (hasLatitude) {
            return restaurantRepositoryOutputPort.findWithinRadius(
                    latitude,
                    longitude,
                    SEARCH_RADIUS_METERS,
                    normalizedSearch
            );
        }
        if (normalizedSearch == null) {
            return restaurantRepositoryOutputPort.findAll();
        }
        return restaurantRepositoryOutputPort.searchByNameOrDescription(normalizedSearch);
    }

    @Override
    public Restaurant create(UUID authenticatedUserId, Restaurant restaurant, Address address) {
        if (address == null) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
        if (!userReferenceRepositoryOutputPort.existsById(authenticatedUserId)) {
            throw new ConflictException(ConstMessagesEnum.OWNER_NOT_FOUND.getMessage());
        }
        if (restaurantRepositoryOutputPort.existsByOwnerId(authenticatedUserId)) {
            throw new ConflictException(ConstMessagesEnum.RESTAURANT_ALREADY_EXISTS.getMessage());
        }

        enrichAddress(address);
        restaurant.setOwnerId(authenticatedUserId);
        restaurant.setAddress(address);
        address.setRestaurant(restaurant);

        Restaurant saved = restaurantRepositoryOutputPort.save(restaurant);
        restaurantCreatedEventOutputPort.publish(saved.getId(), saved.getOwnerId());
        return saved;
    }

    @Override
    public Restaurant update(
            UUID authenticatedUserId,
            UUID restaurantId,
            Restaurant restaurant,
            Address address
    ) {
        Restaurant existing = findById(restaurantId);
        assertOwner(authenticatedUserId, existing);

        existing.setName(restaurant.getName());
        existing.setDescription(restaurant.getDescription());

        if (address != null) {
            enrichAddress(address);
            Address existingAddress = existing.getAddress();
            if (existingAddress == null) {
                existing.setAddress(address);
                address.setRestaurant(existing);
            } else {
                existingAddress.setCep(address.getCep());
                existingAddress.setLogradouro(address.getLogradouro());
                existingAddress.setNumero(address.getNumero());
                existingAddress.setComplemento(address.getComplemento());
                existingAddress.setBairro(address.getBairro());
                existingAddress.setCidade(address.getCidade());
                existingAddress.setUf(address.getUf());
                existingAddress.setReferencia(address.getReferencia());
                existingAddress.setLatitude(address.getLatitude());
                existingAddress.setLongitude(address.getLongitude());
            }
        }

        return restaurantRepositoryOutputPort.save(existing);
    }

    @Override
    public Restaurant uploadImage(
            UUID authenticatedUserId,
            UUID restaurantId,
            InputStream body,
            long contentLength,
            String contentType
    ) {
        Restaurant existing = findById(restaurantId);
        assertOwner(authenticatedUserId, existing);
        if (body == null || contentLength <= 0) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }
        String key = "restaurants/" + restaurantId + "/profile";
        objectStorageOutputPort.put(key, body, contentLength, contentType.toLowerCase());
        existing.setImageKey(key);
        return restaurantRepositoryOutputPort.save(existing);
    }

    @Override
    public Restaurant deleteImage(UUID authenticatedUserId, UUID restaurantId) {
        Restaurant existing = findById(restaurantId);
        assertOwner(authenticatedUserId, existing);
        if (existing.getImageKey() != null) {
            objectStorageOutputPort.delete(existing.getImageKey());
            existing.setImageKey(null);
        }
        return restaurantRepositoryOutputPort.save(existing);
    }

    @Override
    public void delete(UUID authenticatedUserId, UUID restaurantId) {
        Restaurant existing = findById(restaurantId);
        assertOwner(authenticatedUserId, existing);
        if (existing.getImageKey() != null) {
            objectStorageOutputPort.delete(existing.getImageKey());
        }
        UUID deletedId = existing.getId();
        restaurantRepositoryOutputPort.delete(existing);
        restaurantDeletedEventOutputPort.publish(deletedId);
    }

    private void enrichAddress(Address address) {
        CepAddress cepAddress = cepLookupOutputPort.findByCep(address.getCep())
                .orElseThrow(() -> new ConflictException(ConstMessagesEnum.INVALID_CEP.getMessage()));
        address.setCep(cepAddress.getCep());
        address.setLogradouro(cepAddress.getLogradouro());
        address.setBairro(cepAddress.getBairro());
        address.setCidade(cepAddress.getCidade());
        address.setUf(cepAddress.getUf());

        GeoCoordinates coordinates = geocodingOutputPort.geocode(address)
                .orElseThrow(() -> new ConflictException(ConstMessagesEnum.INVALID_LOCATION.getMessage()));
        address.setLatitude(coordinates.getLatitude());
        address.setLongitude(coordinates.getLongitude());
    }

    private void assertOwner(UUID authenticatedUserId, Restaurant restaurant) {
        if (authenticatedUserId == null || !authenticatedUserId.equals(restaurant.getOwnerId())) {
            throw new ForbiddenException(ConstMessagesEnum.ACCESS_DENIED.getMessage());
        }
    }
}
