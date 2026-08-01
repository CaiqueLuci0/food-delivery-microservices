package food.delivery.user_ms.infra.adapters.outbound.http.viacep;

import food.delivery.user_ms.core.application.ports.out.CepLookupOutputPort;
import food.delivery.user_ms.core.domain.entities.CepAddress;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Component
public class ViaCepLookupAdapter implements CepLookupOutputPort {

    private final RestClient viaCepRestClient;

    public ViaCepLookupAdapter(@Qualifier("viaCepRestClient") RestClient viaCepRestClient) {
        this.viaCepRestClient = viaCepRestClient;
    }

    @Override
    public Optional<CepAddress> findByCep(String cep) {
        String normalizedCep = normalizeCep(cep);
        if (normalizedCep.length() != 8) {
            return Optional.empty();
        }

        try {
            ViaCepResponse response = viaCepRestClient.get()
                    .uri("/ws/{cep}/json/", normalizedCep)
                    .retrieve()
                    .body(ViaCepResponse.class);

            if (response == null || response.hasError()) {
                return Optional.empty();
            }

            return Optional.of(new CepAddress(
                    response.getCep(),
                    response.getLogradouro(),
                    response.getBairro(),
                    response.getLocalidade(),
                    response.getUf()
            ));
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    private String normalizeCep(String cep) {
        if (cep == null) {
            return "";
        }
        return cep.replaceAll("\\D", "");
    }
}
