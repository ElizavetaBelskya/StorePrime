package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.OrderApi;
import ru.tinkoff.storePrime.dto.order.OrderDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {


    @Override
    public ResponseEntity<OrderDto> createOrder(UserDetailsImpl userDetailsImpl, List<Long> pa) {
        return null;
    }
}
