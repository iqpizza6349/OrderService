package me.iqpizza.domain.staff.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity(name = "quantity")
@Table(schema = "order_batch_db")
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Sales sales;

    private String name;

    private int qty;


}
