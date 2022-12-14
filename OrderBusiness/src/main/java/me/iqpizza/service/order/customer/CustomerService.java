package me.iqpizza.service.order.customer;

import lombok.RequiredArgsConstructor;
import me.iqpizza.config.security.dto.AuthenticateUser;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.order.dto.ItemDto;
import me.iqpizza.domain.order.dto.OrderDto;
import me.iqpizza.domain.order.dto.PaymentDto;
import me.iqpizza.domain.order.entity.LineItem;
import me.iqpizza.domain.order.entity.Order;
import me.iqpizza.domain.order.entity.OrderPayment;
import me.iqpizza.domain.order.entity.ShippingAddress;
import me.iqpizza.domain.order.repository.OrderRepository;
import me.iqpizza.domain.order.ro.OrderListRO;
import me.iqpizza.domain.order.ro.OrderRO;
import me.iqpizza.domain.order.ro.bill.BillRO;
import me.iqpizza.service.member.MemberService;
import me.iqpizza.service.order.OrderFindService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final OrderRepository orderRepository;
    private final OrderFindService orderFindService;
    private final MemberService memberService;

    public BillRO placeAnOrder(AuthenticateUser user, final OrderDto orderDto) {
        Member member = memberService.findMemberById(user.getId());
        ShippingAddress shippingAddress = ShippingAddress.builder()
                .zipCode(orderDto.getZipCode())
                .recipient(orderDto.getRecipient())
                .build();
        Set<LineItem> items = orderDto.getItems().stream()
                .map(ItemDto::toEntity).collect(Collectors.toSet());
        long amount = orderDto.getItems().stream()
                .mapToInt(value -> value.getPrice() * value.getQty())
                .sum();
        PaymentDto paymentDto = orderDto.getPaymentDto();
        OrderPayment.PaymentMethod paymentMethod = paymentDto.getPaymentMethod();
        String content = null;
        if (paymentMethod == OrderPayment.PaymentMethod.CREDIT_CARD) {
            content = paymentDto.getCardNumber();
            if (content == null) {
                throw new IllegalArgumentException();
            }
        }
        else if (paymentMethod == OrderPayment.PaymentMethod.MOBILE_PHONE) {
            content = paymentDto.getPhoneNumber();
            if (content == null) {
                throw new IllegalArgumentException();
            }
        }
        Order order = Order.builder()
                .member(member)
                .orderedAt(LocalDate.now())
                .shippingAddress(shippingAddress)
                .build();
        shippingAddress.setOrder(order);
        items.forEach(order::addItem);
        order.addOrUpdatePaymentMethod(amount, paymentMethod, content);
        return new BillRO(orderRepository.save(order));
    }

    public OrderRO cancelOrder(AuthenticateUser user, final long orderId) {
        Member member = memberService.findMemberById(user.getId());
        Order order = orderFindService.findByIdAndMember(orderId, member);
        Order.OrderState orderState = order.getState();
        if (!orderState.isShippingChangeable()
                || orderState == Order.OrderState.CANCELED) {
            // ????????? ???????????? ??????, ?????? ??????
            throw new Order.IrrevocableOrderException();
        }

        order.setState(Order.OrderState.CANCELED);
        return new OrderRO(orderRepository.save(order));
    }

    public OrderListRO findByMember(AuthenticateUser user, final int page) {
        Member member = memberService.findMemberById(user.getId());
        Pageable pageable = PageRequest.of(page, 10);
        return new OrderListRO(orderFindService.findByMember(member, pageable)
                .stream().map(OrderRO::new).collect(Collectors.toList()));
    }
}
