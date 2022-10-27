package me.iqpizza.domain.order.ro;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import me.iqpizza.domain.order.entity.Order;

/**
 * {@link me.iqpizza.domain.order.ro.bill.BillRO} 를 위해 사전에 보내는 Response Object 클래스 <br>
 * 간단한 정보들을 가지고 있음
 */
@Getter
public class OrderRO {

    @JsonProperty("order_id")
    private final Long id;

    private final Order.OrderState state;

    public OrderRO(Order order) {
        this.id = order.getId();
        this.state = order.getState();
    }
}
