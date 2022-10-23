package me.iqpizza.domain.order.entity.payment;

import me.iqpizza.domain.order.entity.OrderPayment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = OrderPayment.PaymentMethod.Values.MOBILE_PHONE)
public class MobilePhonePayment extends OrderPayment {
    
    private String phoneNumber;

}
