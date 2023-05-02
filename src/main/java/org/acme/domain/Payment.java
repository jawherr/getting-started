package org.acme.domain;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.acme.domain.enums.PaymentStatus;
import java.math.BigDecimal;

/**
 * A Payment.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "payments")
public class Payment extends AbstractEntity {

    @NotNull
    @Column(name = "paypal_payment_id")
    private String paypalPaymentId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @NotNull
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
}
