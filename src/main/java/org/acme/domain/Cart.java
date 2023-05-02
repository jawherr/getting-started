package org.acme.domain;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.acme.domain.enums.CartStatus;

/**
 * A Cart.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart extends AbstractEntity {

    @ManyToOne
    private Customer customer;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CartStatus status;
}
