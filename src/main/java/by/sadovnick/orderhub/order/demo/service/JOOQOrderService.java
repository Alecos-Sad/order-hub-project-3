package by.sadovnick.orderhub.order.demo.service;

import by.sadovnick.orderhub.order.CreateOrderRequest;
import by.sadovnick.orderhub.order.Order;
import by.sadovnick.orderhub.order.OrderItem;
import by.sadovnick.orderhub.order.OrderStatus;
import by.sadovnick.orderhub.order.demo.repository.JDBCOrderRepository;
import by.sadovnick.orderhub.order.demo.repository.JOOQOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JOOQOrderService {

    private final JOOQOrderRepository jooqOrderRepository;

    public Order createWithJOOQ(CreateOrderRequest createOrderRequest) {
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
        items.forEach(order::addItem);
        return jooqOrderRepository.save(order);
    }
}
