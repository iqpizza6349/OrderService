package me.iqpizza.domain.order.ro.item;

import lombok.Getter;
import me.iqpizza.domain.order.entity.LineItem;

@Getter
public class ItemRO {

    private final String name;
    private final int price;
    private final short qty;

    public ItemRO(LineItem item) {
        this.name = item.getName();
        this.price = item.getPrice();
        this.qty = item.getQty();
    }
}
