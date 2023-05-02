package org.acme.domain;

import io.smallrye.common.constraint.NotNull;
import javax.persistence.*;
import lombok.*;
import org.acme.domain.enums.PaymentStatus;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Payment.
 */
@Getter
@Setter
@NoArgsConstructor
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

    public Payment(String paypalPaymentId, @NotNull PaymentStatus status,
                   @NotNull BigDecimal amount) {
        this.paypalPaymentId = paypalPaymentId;
        this.status = status;
        this.amount = amount;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paypalPaymentId, payment.paypalPaymentId);
    }
    @Override
    public int hashCode() { return Objects.hash(paypalPaymentId); }
}
