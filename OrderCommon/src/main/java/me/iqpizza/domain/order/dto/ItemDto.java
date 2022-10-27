package me.iqpizza.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.iqpizza.domain.order.entity.LineItem;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class ItemDto {

    @NotBlank
    private String name;

    @Min(1)
    private int price;

    @Min(1)
    @Max(255)
    private short qty;

    public LineItem toEntity() {
        return LineItem.builder()
                .name(name)
                .price(price)
                .qty(qty)
                .build();
    }
}
