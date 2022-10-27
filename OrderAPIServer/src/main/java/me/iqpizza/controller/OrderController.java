package me.iqpizza.controller;

import lombok.RequiredArgsConstructor;
import me.iqpizza.config.security.dto.AuthenticateUser;
import me.iqpizza.domain.order.dto.OrderDto;
import me.iqpizza.domain.order.ro.OrderRO;
import me.iqpizza.domain.order.ro.bill.BillRO;
import me.iqpizza.global.valiadator.CollectionValidator;
import me.iqpizza.service.order.OrderService;
import me.iqpizza.service.order.customer.CustomerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final CollectionValidator collectionValidator;

    @GetMapping
    public OrderRO findOrderState(@RequestParam("order-id") long orderId) {
        return orderService.findOrderState(orderId);
    }

    @PostMapping
    public BillRO placeAnOrder(@AuthenticationPrincipal AuthenticateUser user,
                               @RequestBody @Valid OrderDto orderDto,
                               BindingResult bindingResult) throws BindException {
        collectionValidator.validate(orderDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return customerService.placeAnOrder(user, orderDto);
    }
}
