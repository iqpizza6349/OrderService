package me.iqpizza.domain.order.entity.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.iqpizza.domain.order.entity.Order;
import me.iqpizza.domain.order.entity.OrderPayment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = OrderPayment.PaymentMethod.Values.CREDIT_CARD)
public class CreditCardPayment extends OrderPayment {

    private String cardNumber;

    public CreditCardPayment(Long id, long amount, PaymentMethod method,
                             Order order, String cardNumber) {
        super(id, amount, method, order);
        this.cardNumber = cardNumber;
    }
}
