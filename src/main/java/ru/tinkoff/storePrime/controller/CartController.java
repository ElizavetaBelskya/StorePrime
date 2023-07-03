package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.CartApi;
import ru.tinkoff.storePrime.dto.cart.CartItemDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.services.CartService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CartController implements CartApi {

    private final CartService cartService;


    @Override
    public ResponseEntity<CartItemDto> addProductToCart(UserDetailsImpl userDetailsImpl, Long productId, Integer quantity) {
        Long customerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addNewCartItem(customerId, productId, quantity));
    }

    @Override
    public ResponseEntity<List<CartItemDto>> getCustomerCart(UserDetailsImpl userDetailsImpl) {
        Long customerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(cartService.getCustomerCart(customerId));
    }

    @Override
    public ResponseEntity<CartItemDto> deleteProductFromCart(UserDetailsImpl userDetailsImpl, Long productId) {
        Long customerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(cartService.deleteProductFromCart(customerId, productId));
    }


}
