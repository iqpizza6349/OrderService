package me.iqpizza.domain.staff.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@Entity(name = "sales")
@Table(schema = "order_batch_db")
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sales {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDate orderAt;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    @ColumnDefault("0")
    private long dailySales = 0;

    @Builder.Default
    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL)
    private Set<Quantity> quantities = new HashSet<>();

    public void addQuantity(Quantity quantity) {
        quantities.add(quantity);
    }

    public void removeQuantity(Quantity quantity) {
        quantities.remove(quantity);
    }
}
