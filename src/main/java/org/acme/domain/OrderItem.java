package org.acme.domain;

import io.smallrye.common.constraint.NotNull;
import javax.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * A OrderItem.
 */
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
@Entity
@Table(name = "order_items")
public class OrderItem extends AbstractEntity {

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Order order;

    public OrderItem(@NotNull Long quantity, Product product, Order order) {
        this.quantity = quantity;
        this.product = product;
        this.order = order;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return  Objects.equals(quantity, orderItem.quantity) &&
                Objects.equals(product, orderItem.product) &&
                Objects.equals(order, orderItem.order);
    }
    @Override
    public int hashCode() { return Objects.hash(quantity, product, order); }
}
