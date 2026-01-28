package by.sadovnick.orderhub.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "order_number", nullable = false)
    private String orderNumber;
    @Column(name = "create_at")
    private Instant createAt;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "order"
    )
    private List<OrderItem> items = new ArrayList<>();

    public Order(List<OrderItem> items) {
        this.status = OrderStatus.CREATED;
        this.createAt = Instant.now();
        this.orderNumber = UUID.randomUUID().toString();
        if (items != null && !items.isEmpty()) {
            items.forEach(this::addItem);
        }
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            return;
        }
        items.add(item);
        item.setOrder(this);
    }
}
