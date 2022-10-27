package me.iqpizza.domain.order.entity;

import lombok.*;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.order.entity.payment.CreditCardPayment;
import me.iqpizza.domain.order.entity.payment.MobilePhonePayment;
import me.iqpizza.global.exception.DomainException;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@Entity(name = "orders")
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Member member;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate orderedAt;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private ShippingAddress shippingAddress;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<LineItem> items = new HashSet<>();

    public void addItem(LineItem item) {
        item.setOrder(this);
        items.add(item);
    }

    public void removeItem(LineItem item) {
        item.setOrder(null);
        items.remove(item);
    }

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderPayment orderPayment;

    public void addOrUpdatePaymentMethod(long amount,
                                         OrderPayment.PaymentMethod method,
                                         String content) {
        OrderPayment orderPayment;
        switch (method) {
            case CREDIT_CARD:
                orderPayment = new CreditCardPayment(null, amount, method, this, content);
                break;
            case MOBILE_PHONE:
                orderPayment = new MobilePhonePayment(null, amount, method, this, content);
                break;
            default: throw new IllegalArgumentException(); // enum exception
        }
        this.orderPayment = orderPayment;
    }

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderState state = OrderState.PAYMENT_WAITING;

    public enum OrderState {
        PAYMENT_WAITING {
            public boolean isShippingChangeable() {
                return true;
            }
        },

        PREPARING {
            public boolean isShippingChangeable() {
                return true;
            }
        },

        SHIPPED,
        DELIVERING,
        DELIVERING_COMPLETED,
        CANCELED;

        public boolean isShippingChangeable() {
            return false;
        }
    }

    public static class NotFoundException extends DomainException {
        public NotFoundException() {
            super(404, "존재하지 않는 주문입니다.");
        }
    }



}
