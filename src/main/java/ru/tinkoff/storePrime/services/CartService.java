package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.cart.CartItemDto;

import java.util.List;

public interface CartService {

    CartItemDto addNewCartItem(Long customerId, Long productId, Integer quantity);

    List<CartItemDto> getCustomerCart(Long customerId);

    CartItemDto deleteProductFromCart(Long customerId, Long productId);
}
