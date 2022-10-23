package me.iqpizza.domain.order.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@ToString
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineItem {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private short qty;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}
