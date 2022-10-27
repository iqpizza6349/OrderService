package me.iqpizza.domain.order.ro.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import me.iqpizza.domain.order.entity.Order;
import me.iqpizza.domain.order.entity.OrderPayment;
import me.iqpizza.domain.order.ro.address.AddressRO;
import me.iqpizza.domain.order.ro.item.ItemRO;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 영수증 Response Object 클래스입니다.
 * 주문인, 수령인, 목적지, 물품, 총 비용, 결제 방식, 주문 요청 날짜
 * 회원이 상세 조회를 할 때에는 해당 클래스를 사용하여 조회합니다.
 */
@Getter
public class BillRO {

    private final String orderer;

    @JsonProperty("order_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",
            timezone = "Asia/Seoul")
    private final LocalDate orderedAt;

    @JsonProperty("address")
    private final AddressRO addressRO;

    @JsonProperty("items")
    private final Set<ItemRO> items;

    @JsonProperty("total_amount")
    private final long totalAmount;

    @JsonProperty("payment_method")
    private final OrderPayment.PaymentMethod paymentMethod;

    public BillRO(Order order) {
        this.orderer = order.getMember().getUsername();
        this.orderedAt = order.getOrderedAt();
        this.addressRO = new AddressRO(order.getShippingAddress());
        this.items = order.getItems().stream()
                .map(ItemRO::new).collect(Collectors.toSet());
        this.totalAmount = order.getOrderPayment().getAmount();
        this.paymentMethod = order.getOrderPayment().getMethod();
    }
}
