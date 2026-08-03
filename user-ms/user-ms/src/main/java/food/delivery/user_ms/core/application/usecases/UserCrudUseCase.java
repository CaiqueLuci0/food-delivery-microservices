package food.delivery.user_ms.core.application.usecases;

import food.delivery.user_ms.core.application.ports.in.UserCrudUseCaseInputPort;
import food.delivery.user_ms.core.application.ports.out.CepLookupOutputPort;
import food.delivery.user_ms.core.application.ports.out.GeocodingOutputPort;
import food.delivery.user_ms.core.application.ports.out.PasswordEncoderOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserCreatedEventOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserDeletedEventOutputPort;
import food.delivery.user_ms.core.application.ports.out.UserRepositoryOutputPort;
import food.delivery.user_ms.core.domain.entities.Address;
import food.delivery.user_ms.core.domain.entities.CepAddress;
import food.delivery.user_ms.core.domain.entities.GeoCoordinates;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.user_ms.core.domain.exceptions.ConflictException;
import food.delivery.user_ms.core.domain.exceptions.ForbiddenException;
import food.delivery.user_ms.core.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

public class UserCrudUseCase implements UserCrudUseCaseInputPort {

    private final UserRepositoryOutputPort userRepositoryOutputPort;
    private final PasswordEncoderOutputPort passwordEncoderOutputPort;
    private final UserCreatedEventOutputPort userCreatedEventOutputPort;
    private final UserDeletedEventOutputPort userDeletedEventOutputPort;
    private final CepLookupOutputPort cepLookupOutputPort;
    private final GeocodingOutputPort geocodingOutputPort;

    public UserCrudUseCase(
            UserRepositoryOutputPort userRepositoryOutputPort,
            PasswordEncoderOutputPort passwordEncoderOutputPort,
            UserCreatedEventOutputPort userCreatedEventOutputPort,
            UserDeletedEventOutputPort userDeletedEventOutputPort,
            CepLookupOutputPort cepLookupOutputPort,
            GeocodingOutputPort geocodingOutputPort
    ) {
        this.userRepositoryOutputPort = userRepositoryOutputPort;
        this.passwordEncoderOutputPort = passwordEncoderOutputPort;
        this.userCreatedEventOutputPort = userCreatedEventOutputPort;
        this.userDeletedEventOutputPort = userDeletedEventOutputPort;
        this.cepLookupOutputPort = cepLookupOutputPort;
        this.geocodingOutputPort = geocodingOutputPort;
    }

    @Override
    public User findById(UUID id) {
        return this.userRepositoryOutputPort.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstMessagesEnum.NOT_FOUND.getMessage()));
    }

    @Override
    public List<User> findAll() {
        return this.userRepositoryOutputPort.findAll();
    }

    @Override
    public User create(User user, Address address) {
        if (address == null) {
            throw new ConflictException(ConstMessagesEnum.INVALID_REQUEST.getMessage());
        }

        enrichAddress(address);

        if (this.userRepositoryOutputPort.existsByEmail(user.getEmail())) {
            throw new ConflictException(ConstMessagesEnum.EMAIL_ALREADY_EXISTS.getMessage());
        }
        user.setPassword(passwordEncoderOutputPort.encode(user.getPassword()));
        user.setAddress(address);
        address.setUser(user);
        User saved = this.userRepositoryOutputPort.save(user);
        userCreatedEventOutputPort.publish(saved.getId());
        return saved;
    }

    @Override
    public User update(UUID authenticatedUserId, UUID userid, User user, Address address) {
        User existingUser = this.findById(userid);
        assertSameUser(authenticatedUserId, existingUser);
        existingUser.setName(user.getName());

        if (address != null) {
            enrichAddress(address);
            Address existingAddress = existingUser.getAddress();
            if (existingAddress == null) {
                existingUser.setAddress(address);
                address.setUser(existingUser);
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

        return this.userRepositoryOutputPort.save(existingUser);
    }

    @Override
    public void delete(UUID authenticatedUserId, UUID userid) {
        User existingUser = this.findById(userid);
        assertSameUser(authenticatedUserId, existingUser);
        UUID deletedUserId = existingUser.getId();
        this.userRepositoryOutputPort.delete(existingUser);
        userDeletedEventOutputPort.publish(deletedUserId);
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

    private void assertSameUser(UUID authenticatedUserId, User existingUser) {
        if (!authenticatedUserId.equals(existingUser.getId())) {
            throw new ForbiddenException(ConstMessagesEnum.ACCESS_DENIED.getMessage());
        }
    }
}
