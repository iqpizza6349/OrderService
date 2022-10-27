package me.iqpizza.service.order;

import lombok.RequiredArgsConstructor;
import me.iqpizza.domain.order.entity.Order;
import me.iqpizza.domain.order.repository.OrderRepository;
import me.iqpizza.domain.order.ro.OrderRO;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderRO findOrderState(final long orderId) {
        return new OrderRO(findById(orderId));
    }

    @Lock(LockModeType.PESSIMISTIC_READ)
    protected Order findById(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(Order.NotFoundException::new);
    }
}
