package food.delivery.restaurant_ms.infra.adapters.outbound.persistence.mappers;

import food.delivery.restaurant_ms.core.domain.entities.Address;
import food.delivery.restaurant_ms.infra.adapters.outbound.persistence.jpaentities.JpaAddress;

public final class AddressPersistenceMapper {

    private AddressPersistenceMapper() {
    }

    public static JpaAddress toJpa(Address address) {
        if (address == null) {
            return null;
        }
        JpaAddress jpaAddress = new JpaAddress();
        jpaAddress.setId(address.getId());
        jpaAddress.setCep(address.getCep());
        jpaAddress.setLogradouro(address.getLogradouro());
        jpaAddress.setNumero(address.getNumero());
        jpaAddress.setComplemento(address.getComplemento());
        jpaAddress.setBairro(address.getBairro());
        jpaAddress.setCidade(address.getCidade());
        jpaAddress.setUf(address.getUf());
        jpaAddress.setReferencia(address.getReferencia());
        jpaAddress.setLatitude(address.getLatitude());
        jpaAddress.setLongitude(address.getLongitude());
        return jpaAddress;
    }

    public static Address toDomain(JpaAddress jpaAddress) {
        if (jpaAddress == null) {
            return null;
        }
        Address address = new Address();
        address.setId(jpaAddress.getId());
        address.setCep(jpaAddress.getCep());
        address.setLogradouro(jpaAddress.getLogradouro());
        address.setNumero(jpaAddress.getNumero());
        address.setComplemento(jpaAddress.getComplemento());
        address.setBairro(jpaAddress.getBairro());
        address.setCidade(jpaAddress.getCidade());
        address.setUf(jpaAddress.getUf());
        address.setReferencia(jpaAddress.getReferencia());
        address.setLatitude(jpaAddress.getLatitude());
        address.setLongitude(jpaAddress.getLongitude());
        return address;
    }
}
