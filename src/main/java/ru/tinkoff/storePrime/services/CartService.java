package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.CartItemDto;

public interface CartService {

    CartItemDto addNewCartItem(Long customerId, Long productId, Integer quantity);

}
