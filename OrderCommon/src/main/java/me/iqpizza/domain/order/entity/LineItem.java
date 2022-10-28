package me.iqpizza.domain.order.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@ToString
@Getter @Setter
@Table(schema = "order_service_db")
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private short qty;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}
