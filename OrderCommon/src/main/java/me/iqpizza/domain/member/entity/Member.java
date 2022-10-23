package me.iqpizza.domain.member.entity;

import lombok.*;
import me.iqpizza.domain.order.entity.Order;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.CUSTOMER;

    public enum MemberRole {
        CUSTOMER, STAFF
    }

    @Builder.Default
    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Order> orders = new HashSet<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }
}