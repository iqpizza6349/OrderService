package me.iqpizza.domain.order.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity(name = "shipping_address")
@Table(schema = "order_service_db")
@AllArgsConstructor @NoArgsConstructor
public class ShippingAddress {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String zipCode;

    private String recipient;

    @Setter
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

}
