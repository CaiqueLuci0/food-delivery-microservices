package food.delivery.user_ms.infra.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GeoapifyProperties.class)
public class GeoapifyConfig {

    @Bean
    RestClient geoapifyRestClient(GeoapifyProperties geoapifyProperties) {
        return RestClient.builder()
                .baseUrl(geoapifyProperties.getBaseUrl())
                .build();
    }
}
