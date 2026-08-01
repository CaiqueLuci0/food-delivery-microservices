package food.delivery.user_ms.core.application.ports.out;

import food.delivery.user_ms.core.domain.entities.CepAddress;

import java.util.Optional;

public interface CepLookupOutputPort {
    Optional<CepAddress> findByCep(String cep);
}
