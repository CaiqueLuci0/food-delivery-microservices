package food.delivery.order_ms.infra.adapters.inbound.web.presenter.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class HttpExceptionMessage {
    private Integer status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

    public HttpExceptionMessage(Integer status, String message) {
        this(status, message, null);
    }

    public HttpExceptionMessage(Integer status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
