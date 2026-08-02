package food.delivery.restaurant_ms.core.domain.exceptions;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
