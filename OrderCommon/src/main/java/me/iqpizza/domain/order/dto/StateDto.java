package me.iqpizza.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.iqpizza.domain.order.entity.Order;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class StateDto {

    private Order.OrderState state;

}
