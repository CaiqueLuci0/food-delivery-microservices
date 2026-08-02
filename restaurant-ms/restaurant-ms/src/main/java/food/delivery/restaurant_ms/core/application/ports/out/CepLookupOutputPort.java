package food.delivery.restaurant_ms.core.application.ports.out;

import food.delivery.restaurant_ms.core.domain.entities.CepAddress;

import java.util.Optional;

public interface CepLookupOutputPort {

    Optional<CepAddress> findByCep(String cep);
}
