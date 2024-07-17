package dev.elephantcode.temperatureprovider.api;

import jakarta.servlet.RequestDispatcher;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiErrorHandler extends DefaultErrorAttributes {

    private final Clock clock;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> internal(RuntimeException exception) {
        log.warn("Unexpected exception", exception);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(Instant.now(clock), "Something went wrong", "INTERNAL_SERVER_ERROR"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> api(ConstraintViolationException exception) {
        log.warn("Constraint violation exception: {}", exception.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(Instant.now(clock), "Invalid request", "BAD_REQUEST"));
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("timestamp", Instant.now(clock));
        errorAttributes.put("message", super.getMessage(webRequest, super.getError(webRequest)));

        try {
            Integer status = (Integer) webRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE, RequestAttributes.SCOPE_REQUEST);
            errorAttributes.put("code", HttpStatus.valueOf(status).name());
        } catch (Exception ex) {
            errorAttributes.put("code", "UNKNOWN_ERROR");
        }
        return errorAttributes;
    }

    public record ErrorResponse(
            Instant timestamp,
            String message,
            String code
    ) {
    }
}
