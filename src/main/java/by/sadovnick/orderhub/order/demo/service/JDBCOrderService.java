package by.sadovnick.orderhub.order.demo.service;

import by.sadovnick.orderhub.order.CreateOrderRequest;
import by.sadovnick.orderhub.order.Order;
import by.sadovnick.orderhub.order.OrderItem;
import by.sadovnick.orderhub.order.OrderStatus;
import by.sadovnick.orderhub.order.demo.repository.JDBCOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JDBCOrderService {

    private final JDBCOrderRepository jdbcOrderRepository;

    public Order createWithJDBC(CreateOrderRequest createOrderRequest) {
        List<OrderItem> items = createOrderRequest.items().stream()
                .map(
                        item -> new OrderItem(
                                item.productId(),
                                item.productName(),
                                item.quantity(),
                                item.price()
                        )
                ).toList();
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCreateAt(Instant.now());
        order.setItems(items);
        return jdbcOrderRepository.save(order);
    }


}
