package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.OrderApi;
import ru.tinkoff.storePrime.dto.order.OrderDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.services.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;


    @Override
    public ResponseEntity<OrderDto> createOrder(UserDetailsImpl userDetailsImpl, List<Long> cartItemIdList) {
        Long customerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createNewOrder(customerId, cartItemIdList));
    }

    @Override
    public ResponseEntity<List<OrderDto>> getAllOrder(UserDetailsImpl userDetailsImpl) {
        Long customerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(orderService.getAllOrdersOfCustomer(customerId));
    }

    @Override
    public ResponseEntity<List<OrderDto>> getAllOrderForSeller(UserDetailsImpl userDetailsImpl) {
        Long sellerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(orderService.getAllOrdersOfSeller(sellerId));
    }

    @Override
    public ResponseEntity<List<OrderDto>> getCancelledProductsForCustomer(UserDetailsImpl userDetailsImpl) {
        Long customerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(orderService.getCancelledOrdersByCustomerId(customerId));
    }

    @Override
    public ResponseEntity<OrderDto> changeStatus(UserDetailsImpl userDetailsImpl, Long orderId, String status) {
        Long sellerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(orderService.changeStatus(sellerId, orderId, status));
    }

    @Override
    public ResponseEntity<OrderDto> cancelOrder(UserDetailsImpl userDetailsImpl, Long orderId) {
        Long customerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(orderService.cancelOrder(customerId, orderId));
    }


}
