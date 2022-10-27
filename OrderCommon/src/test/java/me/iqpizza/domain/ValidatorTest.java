package me.iqpizza.domain;

import me.iqpizza.domain.order.dto.ItemDto;
import me.iqpizza.domain.order.dto.OrderDto;
import me.iqpizza.domain.order.dto.PaymentDto;
import me.iqpizza.domain.order.entity.OrderPayment;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidatorTest {

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void init() {
        validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(HibernateValidator.class);
        validator.afterPropertiesSet();
    }

    private PaymentDto paymentDto() {
        return new PaymentDto(OrderPayment.PaymentMethod.CREDIT_CARD, null,
                "1234-5678-1234-5789");
    }

    private ItemDto itemDto(String name, int price, int qty) {
        return new ItemDto(name, price, (short) qty);
    }

    private OrderDto orderDto(ItemDto... itemDto) {
        return new OrderDto("iqpizza6349", "00000-00000", Set.of(itemDto), paymentDto());
    }

    @Test
    @DisplayName("필드 Valid 실패 테스트")
    void fieldDtoValidFailTest() {
        // given
        OrderDto orderDto = orderDto();
    
        // when
        Set<ConstraintViolation<OrderDto>> violations = validator.validate(orderDto);
        
        // then
        assertThat(violations.isEmpty()).isFalse();
    }
    
    @Test
    @DisplayName("필드 Valid 성공 테스트")
    void fieldDtoValidSuccessTest() {
        // given
        OrderDto orderDto = orderDto(itemDto("apple", 10000, 36));

        // when
        Set<ConstraintViolation<OrderDto>> violations = validator.validate(orderDto);

        // then
        assertThat(violations.isEmpty()).isTrue();
    }
    
    @Test
    @DisplayName("필드 Collection(Set) Valid 실패 1차 테스트")
    void fieldCollectionValidFailedTest() {
        // given
        OrderDto orderDto = orderDto(itemDto("", 0, 0));
    
        // when
        Set<ConstraintViolation<OrderDto>> violations = validator.validate(orderDto);

        // then
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("필드 Collection(Set) Valid 실패 2차 테스트")
    void fieldCollectionValidFailedTest2() {
        // given
        OrderDto orderDto = orderDto(itemDto("juice", 10, 1), itemDto("", 0, 0));

        // when
        Set<ConstraintViolation<OrderDto>> violations = validator.validate(orderDto);

        // then
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("필드 Collection(Set) Valid 성공 테스트")
    void fieldCollectionValidSuccessTest() {
        // given
        OrderDto orderDto = orderDto(itemDto("apple", 10000, 36));

        // when
        Set<ConstraintViolation<OrderDto>> violations = validator.validate(orderDto);

        // then
        assertThat(violations.isEmpty()).isTrue();
    }
}
