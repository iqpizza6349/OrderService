package me.iqpizza.domain.order.entity.payment;

import me.iqpizza.domain.order.entity.OrderPayment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = OrderPayment.PaymentMethod.Values.CREDIT_CARD)
public class CreditCardPayment extends OrderPayment {

    private String cardNumber;

}
