package me.iqpizza.service.order;

import lombok.RequiredArgsConstructor;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.order.entity.Order;
import me.iqpizza.domain.order.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderFindService {

    private final OrderRepository orderRepository;

    @Lock(LockModeType.PESSIMISTIC_READ)
    public Order findById(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(Order.NotFoundException::new);
    }

    @Lock(LockModeType.PESSIMISTIC_READ)
    public Order findByIdAndMember(long orderId, Member member) {
        return orderRepository.findByIdAndMember(orderId, member)
                .orElseThrow(Order.NotFoundException::new);
    }

    @Lock(LockModeType.PESSIMISTIC_READ)
    public List<Order> findByMember(Member member, Pageable pageable) {
        return orderRepository.findAllByMember(member, pageable).toList();
    }
}
