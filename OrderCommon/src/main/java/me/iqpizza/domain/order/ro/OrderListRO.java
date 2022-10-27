package me.iqpizza.domain.order.ro;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 페이징을 통해 여러 개를 한 번에 조회하게 되는 경우,
 * {@link OrderRO} 클래스를 {@link List} 클래스를 사용하여 목록 형태로 조회함
 */
@Getter
@AllArgsConstructor
public class OrderListRO {

    private final List<OrderRO> orders;

}
