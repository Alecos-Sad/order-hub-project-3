package by.sadovnick.orderhub.order.demo.repository;

import by.sadovnick.orderhub.order.Order;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.tables.OrderItems;
import org.jooq.generated.tables.Orders;
import org.jooq.generated.tables.records.OrderItemsRecord;
import org.jooq.generated.tables.records.OrdersRecord;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.jooq.generated.Tables.ORDERS;
import static org.jooq.generated.Tables.ORDER_ITEMS;

@Repository
@RequiredArgsConstructor
public class JOOQOrderRepository {

    // DSLContext - основной интерфейс jOOQ для построения запросов
    // Это типобезопасный DSL (Domain Specific Language) для SQL
    private final DSLContext dsl;

    public Order save(Order order) {
        // СОХРАНЕНИЕ ORDER С ИСПОЛЬЗОВАНИЕМ jOOQ RECORD

        // Создаем новый Record для таблицы orders
        // Record - это типобезопасное представление строки таблицы
        OrdersRecord orderRecord = dsl.newRecord(ORDERS);

        // Устанавливаем значение статуса
        // ORDERS.STATUS - это сгенерированное константное поле
        // Преобразуем enum OrderStatus в String
        orderRecord.setStatus(order.getStatus().name());

        // Конвертируем Instant в OffsetDateTime для PostgreSQL
        // PostgreSQL хранит даты с часовым поясом (TIMESTAMP WITH TIME ZONE)
        // ZoneOffset.UTC - используем UTC для консистентности
        OffsetDateTime offsetDateTime = order.getCreateAt().atOffset(ZoneOffset.UTC);

        // Устанавливаем дату создания и номер заказа
        // Все setter'ы типобезопасны - IDE будет подсказывать доступные методы
        orderRecord.setCreateAt(offsetDateTime);
        orderRecord.setOrderNumber(order.getOrderNumber());

        // Сохраняем запись в базу данных
        // Метод store() выполняет INSERT или UPDATE в зависимости от состояния Record
        // После сохранения Record автоматически заполняется сгенерированным ID
        orderRecord.store();  // Сохраняем и получаем сгенерированный ID

        // Получаем сгенерированный ID из сохраненной записи
        Long orderId = orderRecord.getId();

        // Устанавливаем ID в исходный объект Order
        order.setId(orderId);

        // СОХРАНЕНИЕ ORDERITEMS (BATCH INSERT)
        // Проверяем, что в заказе есть позиции
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            // Создаем список Records для позиций заказа
            List<OrderItemsRecord> itemRecords = order.getItems().stream()
                    .map(item -> {
                        // Создаем новый Record для таблицы order_items
                        OrderItemsRecord record = dsl.newRecord(ORDER_ITEMS);

                        // Устанавливаем значения полей
                        // Все поля типобезопасны - компилятор проверит правильность
                        record.setOrderId(orderId);          // Связь с родительским заказом
                        record.setProductId(item.getProductId());   // ID продукта
                        record.setProductName(item.getProductName()); // Название продукта
                        record.setQuantity(item.getQuantity());     // Количество
                        record.setPrice(item.getPrice());           // Цена

                        return record;
                    })
                    .toList();  // Собираем в неизменяемый список

            // Выполняем batch insert всех записей
            // batchInsert оптимизирован для вставки множества строк одним запросом
            dsl.batchInsert(itemRecords).execute();
        }

        // Возвращаем обновленный объект Order с установленным ID
        return order;
    }
}