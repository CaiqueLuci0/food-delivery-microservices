package food.delivery.restaurant_ms.infra.config;

import food.delivery.restaurant_ms.infra.adapters.outbound.messaging.event.UserCreatedEvent;
import food.delivery.restaurant_ms.infra.adapters.outbound.messaging.event.UserDeletedEvent;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(MessagingProperties.class)
public class MessagingConfig {

    @Bean
    MessageConverter jacksonMessageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put(
                "food.delivery.user_ms.infra.adapters.outbound.messaging.event.UserCreatedEvent",
                UserCreatedEvent.class
        );
        idClassMapping.put(
                "food.delivery.user_ms.infra.adapters.outbound.messaging.event.UserDeletedEvent",
                UserDeletedEvent.class
        );
        classMapper.setIdClassMapping(idClassMapping);
        classMapper.setTrustedPackages("*");
        converter.setClassMapper(classMapper);
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }
}
