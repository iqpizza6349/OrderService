package me.iqpizza.domain.order.entity.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.iqpizza.domain.order.entity.Order;
import me.iqpizza.domain.order.entity.OrderPayment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = OrderPayment.PaymentMethod.Values.MOBILE_PHONE)
public class MobilePhonePayment extends OrderPayment {
    
    private String phoneNumber;

    public MobilePhonePayment(Long id, long amount, PaymentMethod method,
                              Order order, String phoneNumber) {
        super(id, amount, method, order);
        this.phoneNumber = phoneNumber;
    }
}
