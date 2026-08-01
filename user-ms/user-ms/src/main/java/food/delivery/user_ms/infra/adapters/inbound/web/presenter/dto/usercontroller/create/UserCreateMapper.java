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
}
