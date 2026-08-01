package food.delivery.user_ms.infra.adapters.inbound.web.presenter.dto.usercontroller.create;

import food.delivery.user_ms.core.domain.entities.Adress;
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

    public static Adress toAdress(AdressRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Adress adress = new Adress();
        adress.setCep(dto.getCep());
        adress.setLogradouro(dto.getLogradouro());
        adress.setNumero(dto.getNumero());
        adress.setComplemento(dto.getComplemento());
        adress.setBairro(dto.getBairro());
        adress.setCidade(dto.getCidade());
        adress.setUf(dto.getUf());
        adress.setReferencia(dto.getReferencia());
        return adress;
    }

    public static UserCreateResponseDto toResponse(User user) {
        UserCreateResponseDto response = new UserCreateResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setAdress(toAdressResponse(user.getAdress()));
        return response;
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
