package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.get;

import food.delivery.user_ms.core.domain.entities.Adress;
import food.delivery.user_ms.core.domain.entities.User;
import food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create.AdressResponseDto;

import java.util.List;

public final class UserResponseMapper {

    private UserResponseMapper() {
    }

    public static UserResponseDto toResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setAdress(toAdressResponse(user.getAdress()));
        return response;
    }

    public static List<UserResponseDto> toResponseList(List<User> users) {
        return users.stream().map(UserResponseMapper::toResponse).toList();
    }

    private static AdressResponseDto toAdressResponse(Adress adress) {
        if (adress == null) {
            return null;
        }
        AdressResponseDto dto = new AdressResponseDto();
        dto.setId(adress.getId());
        dto.setCep(adress.getCep());
        dto.setLogradouro(adress.getLogradouro());
        dto.setNumero(adress.getNumero());
        dto.setComplemento(adress.getComplemento());
        dto.setBairro(adress.getBairro());
        dto.setCidade(adress.getCidade());
        dto.setUf(adress.getUf());
        dto.setReferencia(adress.getReferencia());
        return dto;
    }
}
