package food.delivery.user_ms.infra.adapters.outbound.persistence.mappers;

import food.delivery.user_ms.core.domain.entities.Adress;
import food.delivery.user_ms.infra.adapters.outbound.persistence.jpaentities.JpaAdress;

public final class AdressPersistenceMapper {

    private AdressPersistenceMapper() {
    }

    public static JpaAdress toJpa(Adress adress) {
        if (adress == null) {
            return null;
        }

        JpaAdress jpaAdress = new JpaAdress();
        jpaAdress.setId(adress.getId());
        jpaAdress.setCep(adress.getCep());
        jpaAdress.setLogradouro(adress.getLogradouro());
        jpaAdress.setNumero(adress.getNumero());
        jpaAdress.setComplemento(adress.getComplemento());
        jpaAdress.setBairro(adress.getBairro());
        jpaAdress.setCidade(adress.getCidade());
        jpaAdress.setUf(adress.getUf());
        jpaAdress.setReferencia(adress.getReferencia());
        return jpaAdress;
    }

    public static Adress toDomain(JpaAdress jpaAdress) {
        if (jpaAdress == null) {
            return null;
        }

        Adress adress = new Adress();
        adress.setId(jpaAdress.getId());
        adress.setCep(jpaAdress.getCep());
        adress.setLogradouro(jpaAdress.getLogradouro());
        adress.setNumero(jpaAdress.getNumero());
        adress.setComplemento(jpaAdress.getComplemento());
        adress.setBairro(jpaAdress.getBairro());
        adress.setCidade(jpaAdress.getCidade());
        adress.setUf(jpaAdress.getUf());
        adress.setReferencia(jpaAdress.getReferencia());
        return adress;
    }
}
