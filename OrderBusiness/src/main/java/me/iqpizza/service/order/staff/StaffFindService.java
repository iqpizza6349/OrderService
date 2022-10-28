package me.iqpizza.service.order.staff;

import lombok.RequiredArgsConstructor;
import me.iqpizza.config.security.dto.AuthenticateUser;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.order.dto.StateDto;
import me.iqpizza.domain.order.entity.Order;
import me.iqpizza.domain.order.repository.OrderRepository;
import me.iqpizza.domain.order.ro.OrderRO;
import me.iqpizza.service.order.OrderFindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StaffFindService {

    private final OrderRepository orderRepository;
    private final OrderFindService orderFindService;

    public OrderRO changeOrderState(AuthenticateUser user, final long orderId,
                                    final StateDto stateDto) {
        Member.MemberRole memberRole = Member.MemberRole.convertFrom(user.getRole());
        if (memberRole != Member.MemberRole.STAFF) {
            // 권한 없음
            throw new IllegalArgumentException();
        }

        Order order = orderFindService.findById(orderId);
        order.setState(stateDto.getState());
        return new OrderRO(orderRepository.save(order));
    }
}
