package by.sadovnick.orderhub.order.service;

import by.sadovnick.orderhub.order.CreateOrderRequest;
import by.sadovnick.orderhub.order.Order;
import by.sadovnick.orderhub.order.OrderItem;
import by.sadovnick.orderhub.order.exception.NotFoundOrderException;
import by.sadovnick.orderhub.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(CreateOrderRequest request) {
        log.debug("В метод createOrder получен запрос: {}", request);
        List<OrderItem> items = request
                .items()
                .stream()
                .map(item -> new OrderItem(
                                item.productId(),
                                item.productName(),
                                item.quantity(),
                                item.price()
                        )
                ).toList();
        Order order = new Order(items);
        log.debug("Все успешно сохранено!");
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrderWithItems(Long id) {
        log.debug("В метод createOrder получен запрос поиска order по id: {}", id);
        return orderRepository.findWithItemsById(id).orElseThrow(
                () -> new NotFoundOrderException("Заказ не найден")
        );
    }

}
