package me.iqpizza.batch.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderQuantity {
    private String name;
    private Integer totalQuantity;
    private Integer totalPrice;

    public int getTotalPrice() {
        return totalQuantity * totalPrice;
    }
}
