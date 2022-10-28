package me.iqpizza.service.order.customer;

import me.iqpizza.config.security.dto.AuthenticateUser;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.order.dto.ItemDto;
import me.iqpizza.domain.order.dto.OrderDto;
import me.iqpizza.domain.order.dto.PaymentDto;
import me.iqpizza.domain.order.entity.Order;
import me.iqpizza.domain.order.entity.OrderPayment;
import me.iqpizza.domain.order.entity.ShippingAddress;
import me.iqpizza.domain.order.entity.payment.MobilePhonePayment;
import me.iqpizza.domain.order.repository.OrderRepository;
import me.iqpizza.domain.order.ro.OrderRO;
import me.iqpizza.domain.order.ro.bill.BillRO;
import me.iqpizza.domain.order.ro.item.ItemRO;
import me.iqpizza.service.member.MemberService;
import me.iqpizza.service.order.OrderFindService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderFindService orderFindService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private CustomerService customerService;

    private Set<ItemDto> items() {
        return Set.of(new ItemDto("apple", 10000, (short) 36));
    }

    private OrderDto orderDto(PaymentDto paymentDto) {
        return new OrderDto(
                "iqpizza6349",
                "00000-00000",
                items(),
                paymentDto
        );
    }

    private Member member() {
        return Member.builder()
                .username("username")
                .password("password")
                .build();
    }

    private void givenMember() {
        given(memberService.findMemberById(anyLong()))
                .willReturn(member());
    }

    private AuthenticateUser user() {
        return new AuthenticateUser(1L, "username", "CUSTOMER");
    }

    private Order orderWithPayment(OrderPayment orderPayment) {
        return Order.builder()
                .id(1L)
                .member(member())
                .shippingAddress(new ShippingAddress(null, "00000-00000", "iqpizza6349", null))
                .orderPayment(orderPayment)
                .items(items().stream().map(ItemDto::toEntity).collect(Collectors.toSet()))
                .build();
    }

    private Order orderWithState(Order.OrderState state) {
        return Order.builder()
                .id(1L)
                .member(member())
                .state(state)
                .build();
    }

    @Test
    @DisplayName("결제 수단만 정해진 상태로 주문")
    void onlyPaymentMethod() {
        // given
        PaymentDto paymentDto = new PaymentDto(
                OrderPayment.PaymentMethod.CREDIT_CARD, null, null);
        OrderDto orderDto = orderDto(paymentDto);
        given(memberService.findMemberById(anyLong()))
                .willReturn(member());

        // when
        assertThrows(IllegalArgumentException.class, () ->
            customerService.placeAnOrder(user(), orderDto)
        );
    }

    @Test
    @DisplayName("주문 성공")
    void placeAnOrderLogicTest() {
        // given
        PaymentDto paymentDto = new PaymentDto(
                OrderPayment.PaymentMethod.MOBILE_PHONE, "010-0000-0000", null
        );
        OrderPayment orderPayment = new MobilePhonePayment(null, 10000 * 36,
                OrderPayment.PaymentMethod.MOBILE_PHONE, null, "010-0000-0000");
        OrderDto orderDto = orderDto(paymentDto);
        givenMember();

        // when
        when(orderRepository.save(any()))
                .thenReturn(orderWithPayment(orderPayment));
        BillRO billRO = customerService.placeAnOrder(user(), orderDto);

        // then
        assertThat(billRO).isNotNull();
        assertThat(billRO.getItems()).isNotNull();
        assertThat(billRO.getItems().size()).isGreaterThan(0);
        List<ItemRO> roList = new ArrayList<>(billRO.getItems());
        assertThat(roList.get(0)).isNotNull();
        ItemRO itemRO = roList.get(0);
        assertThat(itemRO.getName()).isEqualTo("apple");
        assertThat(itemRO.getPrice()).isEqualTo(10000);
        assertThat(itemRO.getQty()).isEqualTo((short) 36);
    }

    @Test
    @DisplayName("배송 중인 주문 취소하기 실패") // 특정 조건에만 취소할 수 있다.
    void cancelOrderFailed() {
        // given
        long orderId = 1L;
        Order.OrderState orderState = Order.OrderState.DELIVERING;
        givenMember();
        given(orderFindService.findByIdAndMember(anyLong(), any()))
                .willReturn(orderWithState(orderState));

        // when
        assertThrows(Order.IrrevocableOrderException.class, () ->
                customerService.cancelOrder(user(), orderId)
        );
    }

    @Test
    @DisplayName("결제 대기 중인 주문 취소하기 성공")
    void cancelOrderSuccess() {
        // given
        long orderId = 1L;
        Order.OrderState orderState = Order.OrderState.PAYMENT_WAITING;
        Order order = orderWithState(orderState);
        givenMember();
        given(orderFindService.findByIdAndMember(anyLong(), any()))
                .willReturn(order);

        // when
        when(orderRepository.save(any())).thenReturn(order);
        OrderRO orderRO = customerService.cancelOrder(user(), orderId);

        // then
        assertThat(orderRO).isNotNull();
        assertThat(orderRO.getId()).isEqualTo(orderId);
        assertThat(orderRO.getState()).isEqualTo(Order.OrderState.CANCELED);
    }
}
