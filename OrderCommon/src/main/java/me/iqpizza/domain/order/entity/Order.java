package me.iqpizza.domain.order.entity;

import lombok.*;
import me.iqpizza.domain.member.entity.Member;
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

    @OneToOne(mappedBy = "order")
    private ShippingAddress shippingAddress;

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private Set<LineItem> items = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private Set<OrderPayment> orderPayments = new HashSet<>();

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
}
