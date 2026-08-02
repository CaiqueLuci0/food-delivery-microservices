package food.delivery.order_ms.infra.config;

import food.delivery.order_ms.infra.adapters.inbound.messaging.event.PaymentEvent;
import food.delivery.order_ms.infra.adapters.inbound.messaging.event.UserCreatedEvent;
import food.delivery.order_ms.infra.adapters.inbound.messaging.event.UserDeletedEvent;
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
        idClassMapping.put(
                "food.delivery.payment_ms.infra.adapters.outbound.messaging.event.PaymentApprovedEvent",
                PaymentEvent.class
        );
        idClassMapping.put(
                "food.delivery.payment_ms.infra.adapters.outbound.messaging.event.PaymentFailedEvent",
                PaymentEvent.class
        );
        idClassMapping.put(
                "food.delivery.payment_ms.infra.adapters.outbound.messaging.event.PaymentEvent",
                PaymentEvent.class
        );
        classMapper.setIdClassMapping(idClassMapping);
        classMapper.setTrustedPackages("*");
        converter.setClassMapper(classMapper);
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }
}
