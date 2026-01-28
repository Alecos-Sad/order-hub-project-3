package by.sadovnick.orderhub.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        @Valid
        @NotEmpty(message = "items не должен быть пустым")
        List<OrderItemRequest> items
) {
    public record OrderItemRequest(
            @NotNull(message = "productId обязателен")
            Long productId,

            @NotBlank(message = "productName не должен быть пустым")
            String productName,

            @Positive(message = "Количество должно быть > 1")
            int quantity,

            @NotNull(message = "price обязателен")
            @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
            BigDecimal price
    ) {
    }
}
