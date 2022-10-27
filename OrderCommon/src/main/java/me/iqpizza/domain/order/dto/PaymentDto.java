package me.iqpizza.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.iqpizza.domain.order.entity.OrderPayment;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class PaymentDto {

    @JsonProperty("payment_method")
    private OrderPayment.PaymentMethod paymentMethod;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String cardNumber;

}
