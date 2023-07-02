package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.dto.CartItemDto;
import ru.tinkoff.storePrime.models.CartItem;
import ru.tinkoff.storePrime.repository.CartRepository;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.repository.ProductRepository;
import ru.tinkoff.storePrime.services.CartService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    @Override
    public CartItemDto addNewCartItem(Long customerId, Long productId, Integer quantity) {
        Optional<CartItem> foundCartItem = cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId);
        CartItem newCartItem;
        if (foundCartItem.isPresent()) {
            newCartItem = foundCartItem.get();
            newCartItem.setQuantity(quantity);
        } else {
            newCartItem = CartItem.builder()
                    .product(productRepository.findById(productId).orElseThrow())
                    .customer(customerRepository.findById(customerId).orElseThrow())
                    .quantity(1)
                    .build();
        }
        CartItem newOrUpdatedCartItem = cartRepository.save(newCartItem);
        return CartItemDto.from(newOrUpdatedCartItem);
    }



}
