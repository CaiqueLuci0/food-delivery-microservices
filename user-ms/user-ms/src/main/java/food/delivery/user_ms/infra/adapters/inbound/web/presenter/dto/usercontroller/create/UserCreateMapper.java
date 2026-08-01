package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create;

import food.delivery.user_ms.core.domain.entities.Address;
import food.delivery.user_ms.core.domain.entities.User;

public final class UserCreateMapper {

    private UserCreateMapper() {
    }

    public static User toUser(UserCreateRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static Address toAddress(AddressRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setCep(dto.getCep());
        address.setLogradouro(dto.getLogradouro());
        address.setNumero(dto.getNumero());
        address.setComplemento(dto.getComplemento());
        address.setBairro(dto.getBairro());
        address.setCidade(dto.getCidade());
        address.setUf(dto.getUf());
        address.setReferencia(dto.getReferencia());
        return address;
    }
}
