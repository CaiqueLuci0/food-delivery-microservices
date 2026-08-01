package food.delivery.user_ms.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging")
public class MessagingProperties {

    private String userCreatedExchange;
    private String userDeletedExchange;

    public String getUserCreatedExchange() {
        return userCreatedExchange;
    }

    public void setUserCreatedExchange(String userCreatedExchange) {
        this.userCreatedExchange = userCreatedExchange;
    }

    public String getUserDeletedExchange() {
        return userDeletedExchange;
    }

    public void setUserDeletedExchange(String userDeletedExchange) {
        this.userDeletedExchange = userDeletedExchange;
    }
}
