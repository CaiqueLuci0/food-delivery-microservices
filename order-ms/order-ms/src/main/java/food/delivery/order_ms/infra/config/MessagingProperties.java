package food.delivery.order_ms.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging")
public class MessagingProperties {

    private String userCreatedQueue;
    private String userDeletedQueue;
    private String orderCreatedQueue;
    private String paymentApprovedQueue;
    private String paymentFailedQueue;

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

    public String getOrderCreatedQueue() {
        return orderCreatedQueue;
    }

    public void setOrderCreatedQueue(String orderCreatedQueue) {
        this.orderCreatedQueue = orderCreatedQueue;
    }

    public String getPaymentApprovedQueue() {
        return paymentApprovedQueue;
    }

    public void setPaymentApprovedQueue(String paymentApprovedQueue) {
        this.paymentApprovedQueue = paymentApprovedQueue;
    }

    public String getPaymentFailedQueue() {
        return paymentFailedQueue;
    }

    public void setPaymentFailedQueue(String paymentFailedQueue) {
        this.paymentFailedQueue = paymentFailedQueue;
    }
}
