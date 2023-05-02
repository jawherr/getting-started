package org.acme.domain;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.util.Objects;
import lombok.*;
import org.acme.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * An Order.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "orders")
public class Order extends AbstractEntity {

    @NotNull
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "shipped")
    private ZonedDateTime shipped;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private Payment payment;

    @Embedded
    private Address shipmentAddress;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<OrderItem> orderItems;

    @OneToOne
    private Cart cart;

    public Order(@NotNull BigDecimal price, @NotNull OrderStatus status,
                 ZonedDateTime shipped, Payment payment, Address
                         shipmentAddress,
                 Set<OrderItem> orderItems, Cart cart) {
        this.price = price;
        this.status = status;
        this.shipped = shipped;
        this.payment = payment;
        this.shipmentAddress = shipmentAddress;
        this.orderItems = orderItems;
        this.cart = cart;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(price, order.price) && status == order.status
                &&
                Objects.equals(shipped, order.shipped) &&
                Objects.equals(payment, order.payment) &&
                Objects.equals(shipmentAddress, order.shipmentAddress) &&
                Objects.equals(orderItems, order.orderItems) &&
                Objects.equals(cart, order.cart);
    }
    @Override
    public int hashCode() {
        return Objects.hash(price, status, shipped, payment,
                shipmentAddress, cart);
    }
}
