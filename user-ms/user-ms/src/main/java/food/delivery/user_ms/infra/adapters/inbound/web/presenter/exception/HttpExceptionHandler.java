package food.delivery.user_ms.infra.adapters.inbound.web.presenter.exception;

import food.delivery.user_ms.core.domain.enums.ConstMessagesEnum;
import food.delivery.user_ms.core.domain.exceptions.ConflictException;
import food.delivery.user_ms.core.domain.exceptions.ForbiddenException;
import food.delivery.user_ms.core.domain.exceptions.NotFoundException;
import food.delivery.user_ms.core.domain.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HttpExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(HttpExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpExceptionMessage> handleNotFound(NotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new HttpExceptionMessage(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<HttpExceptionMessage> handleConflict(ConflictException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new HttpExceptionMessage(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<HttpExceptionMessage> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new HttpExceptionMessage(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<HttpExceptionMessage> handleForbidden(ForbiddenException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new HttpExceptionMessage(HttpStatus.FORBIDDEN.value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpExceptionMessage> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new HttpExceptionMessage(
                        HttpStatus.BAD_REQUEST.value(),
                        ConstMessagesEnum.VALIDATION_FAILED.getMessage(),
                        errors
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpExceptionMessage> handleException(Exception e) {
        log.error("e: ", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HttpExceptionMessage(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ConstMessagesEnum.INTERNAL_ERROR.getMessage()
                ));
    }
}
