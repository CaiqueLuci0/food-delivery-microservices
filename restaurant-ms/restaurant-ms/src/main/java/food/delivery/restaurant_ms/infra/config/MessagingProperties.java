package food.delivery.restaurant_ms.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging")
public class MessagingProperties {

    private String userCreatedQueue;
    private String userDeletedQueue;
    private String restaurantCreatedQueue;
    private String restaurantDeletedQueue;

    public String getUserCreatedQueue() {
        return userCreatedQueue;
    }

    public void setUserCreatedQueue(String userCreatedQueue) {
        this.userCreatedQueue = userCreatedQueue;
    }

    public String getUserDeletedQueue() {
        return userDeletedQueue;
    }

    public void setUserDeletedQueue(String userDeletedQueue) {
        this.userDeletedQueue = userDeletedQueue;
    }

    public String getRestaurantCreatedQueue() {
        return restaurantCreatedQueue;
    }

    public void setRestaurantCreatedQueue(String restaurantCreatedQueue) {
        this.restaurantCreatedQueue = restaurantCreatedQueue;
    }

    public String getRestaurantDeletedQueue() {
        return restaurantDeletedQueue;
    }

    public void setRestaurantDeletedQueue(String restaurantDeletedQueue) {
        this.restaurantDeletedQueue = restaurantDeletedQueue;
    }
}
