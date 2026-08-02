package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get;

import food.delivery.user_ms.core.domain.entities.Address;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.AddressResponseDto;

import java.util.List;

public final class UserResponseMapper {

    private UserResponseMapper() {
    }

    public static UserResponseDto toResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setAddress(toAddressResponse(user.getAddress()));
        return response;
    }

    public static List<UserResponseDto> toResponseList(List<User> users) {
        return users.stream().map(UserResponseMapper::toResponse).toList();
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
