package me.iqpizza.service.order;

import lombok.RequiredArgsConstructor;
import me.iqpizza.domain.order.ro.OrderRO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderFindService orderFindService;

    public OrderRO findOrderState(final long orderId) {
        return new OrderRO(orderFindService.findById(orderId));
    }
}
