package me.iqpizza.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity(name = "shipping_address")
@AllArgsConstructor @NoArgsConstructor
public class ShippingAddress {

    @Id @GeneratedValue
    private Long id;

    @Column(length = 10, nullable = false)
    private String zipCode;

    private String recipient;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

}
