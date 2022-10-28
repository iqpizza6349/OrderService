package me.iqpizza.domain.staff.ro;

import lombok.Getter;
import me.iqpizza.domain.staff.entity.Quantity;

@Getter
public class QuantityRO {

    private final String name;
    private final int quantity;

    public QuantityRO(Quantity quantity) {
        this.name = quantity.getName();
        this.quantity = quantity.getQty();
    }
}
