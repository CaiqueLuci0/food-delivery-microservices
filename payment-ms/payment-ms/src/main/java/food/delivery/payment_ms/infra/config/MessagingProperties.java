package food.delivery.payment_ms.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging")
public class MessagingProperties {

    private String orderCreatedQueue;
    private String orderDeletedQueue;
    private String paymentApprovedQueue;
    private String paymentFailedQueue;

    public String getOrderCreatedQueue() {
        return orderCreatedQueue;
    }

    public void setOrderCreatedQueue(String orderCreatedQueue) {
        this.orderCreatedQueue = orderCreatedQueue;
    }

    public String getOrderDeletedQueue() {
        return orderDeletedQueue;
    }

    public void setOrderDeletedQueue(String orderDeletedQueue) {
        this.orderDeletedQueue = orderDeletedQueue;
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
