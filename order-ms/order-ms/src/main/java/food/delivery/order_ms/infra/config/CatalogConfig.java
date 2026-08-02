package food.delivery.order_ms.infra.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(CatalogProperties.class)
public class CatalogConfig {

    @Bean
    RestClient catalogRestClient(CatalogProperties catalogProperties) {
        return RestClient.builder()
                .baseUrl(catalogProperties.getBaseUrl())
                .build();
    }
}
