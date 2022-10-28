package me.iqpizza.controller;

import lombok.RequiredArgsConstructor;
import me.iqpizza.config.security.dto.AuthenticateUser;
import me.iqpizza.domain.order.dto.OrderDto;
import me.iqpizza.domain.order.dto.StateDto;
import me.iqpizza.domain.order.ro.OrderListRO;
import me.iqpizza.domain.order.ro.OrderRO;
import me.iqpizza.domain.order.ro.bill.BillRO;
import me.iqpizza.domain.staff.ro.SalesRO;
import me.iqpizza.domain.staff.service.StaffService;
import me.iqpizza.global.valiadator.CollectionValidator;
import me.iqpizza.service.order.OrderService;
import me.iqpizza.service.order.customer.CustomerService;
import me.iqpizza.service.order.staff.StaffFindService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final StaffFindService staffFindService;
    private final StaffService staffService;
    private final CollectionValidator collectionValidator;

    @GetMapping("/{order-id}")
    public OrderRO findOrderState(@PathVariable("order-id") long orderId) {
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

    @DeleteMapping
    public OrderRO cancelOrder(@AuthenticationPrincipal AuthenticateUser user,
                               @RequestParam("order-id") long orderId) {
        return customerService.cancelOrder(user, orderId);
    }

    @GetMapping
    public OrderListRO findOrders(@AuthenticationPrincipal AuthenticateUser user,
                                  @RequestParam(defaultValue = "0") int page) {
        return customerService.findByMember(user, page);
    }

    @PatchMapping("/{order-id}")
    public OrderRO changeState(@PathVariable("order-id") long orderId,
                               @AuthenticationPrincipal AuthenticateUser user,
                               @RequestBody @Valid StateDto stateDto) {
        return staffFindService.changeOrderState(user, orderId, stateDto);
    }

    @GetMapping("/sales")
    public SalesRO getSalesByDate(@AuthenticationPrincipal AuthenticateUser user,
                                  @RequestParam("sales-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate salesDate) {
        return staffService.getSalesDay(user, salesDate);
    }
}
