package me.iqpizza.domain.order.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import javax.persistence.*;

@Getter
@Inheritance
@Entity(name = "order_payment")
@DiscriminatorColumn(name = "method")
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class OrderPayment {

    @Id @GeneratedValue
    private Long id;

    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false, nullable = false)
    private PaymentMethod method;

    public enum PaymentMethod {
        CREDIT_CARD(Values.CREDIT_CARD),
        MOBILE_PHONE(Values.MOBILE_PHONE);

        private String value;

        PaymentMethod(String value) {
            if (!this.name().equals(value)) {
                throw new IllegalArgumentException("Incorrect use of PaymentMethod");
            }

            this.value = value;
        }

        public static class Values {
            public static final String CREDIT_CARD = "CREDIT_CARD";
            public static final String MOBILE_PHONE = "MOBILE_PHONE";
        }

        @JsonCreator
        public static PaymentMethod from(String s) {
            return PaymentMethod.valueOf(s.toUpperCase());
        }
    }

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}
