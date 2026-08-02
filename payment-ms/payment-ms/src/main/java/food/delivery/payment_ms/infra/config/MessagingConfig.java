package food.delivery.payment_ms.infra.config;

import food.delivery.payment_ms.infra.adapters.inbound.messaging.event.OrderCreatedEvent;
import food.delivery.payment_ms.infra.adapters.inbound.messaging.event.OrderDeletedEvent;
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
                "food.delivery.order_ms.infra.adapters.outbound.messaging.event.OrderCreatedEvent",
                OrderCreatedEvent.class
        );
        idClassMapping.put(
                "food.delivery.order_ms.infra.adapters.outbound.messaging.event.OrderDeletedEvent",
                OrderDeletedEvent.class
        );
        classMapper.setIdClassMapping(idClassMapping);
        classMapper.setTrustedPackages("*");
        converter.setClassMapper(classMapper);
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }
}
