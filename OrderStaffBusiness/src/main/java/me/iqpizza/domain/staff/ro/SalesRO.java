package me.iqpizza.domain.staff.ro;

import lombok.Getter;
import me.iqpizza.domain.staff.entity.Sales;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SalesRO {

    private final long amount;

    private final List<QuantityRO> quantities;

    public SalesRO(Sales sales) {
        this.amount = sales.getDailySales();
        this.quantities = sales.getQuantities().stream()
                .map(QuantityRO::new).collect(Collectors.toList());
    }
}
