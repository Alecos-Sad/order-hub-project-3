package by.sadovnick.orderhub.order.advice;

import by.sadovnick.orderhub.order.ApiError;
import by.sadovnick.orderhub.order.exception.NotFoundOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleException(MethodArgumentNotValidException ex) {
        List<String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(
                        error -> error.getField() + " : " + error.getDefaultMessage()
                )
                .toList();
        return ResponseEntity.badRequest().body(
                new ApiError(
                        ex.getStatusCode().value(),
                        "Validation Failed",
                        errors
                )
        );
    }

    @ExceptionHandler(NotFoundOrderException.class)
    public ResponseEntity<ApiError> handleException(NotFoundOrderException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiError(
                        HttpStatus.NOT_FOUND.value(),
                        "Order not found",
                        ex.getMessage()
                )
        );
    }
}
