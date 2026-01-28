package by.sadovnick.orderhub.order.demo.controller;

import by.sadovnick.orderhub.order.CreateOrderRequest;
import by.sadovnick.orderhub.order.Order;
import by.sadovnick.orderhub.order.OrderResponse;
import by.sadovnick.orderhub.order.demo.service.JDBCOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demo/orders")
public class JDBCOrderController {

    private final JDBCOrderService jdbcrderService;

    @PostMapping("/jdbc")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest createOrderRequest
    ) {
        Order withJDBC = jdbcrderService.createWithJDBC(createOrderRequest);
        OrderResponse orderResponse = OrderResponse.from(withJDBC);
        return ResponseEntity.ok(orderResponse);
    }
}
