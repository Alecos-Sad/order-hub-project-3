package by.sadovnick.orderhub.order.exception;

public class NotFoundOrderException extends RuntimeException {
    public NotFoundOrderException(String errorMessage) {
        super(errorMessage);
    }
}
