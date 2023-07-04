package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.dto.cart.CartItemDto;
import ru.tinkoff.storePrime.exceptions.NotFoundException;
import ru.tinkoff.storePrime.models.CartItem;
import ru.tinkoff.storePrime.models.Product;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.repository.CartRepository;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.repository.ProductRepository;
import ru.tinkoff.storePrime.services.CartService;

import java.util.List;
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
        CartItem newCartItem = foundCartItem.orElseGet(() -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new NotFoundException("Customer not found"));
            return CartItem.builder()
                    .product(product)
                    .customer(customer)
                    .quantity(1)
                    .build();
        });
        newCartItem.setQuantity(quantity);
        CartItem newOrUpdatedCartItem = cartRepository.save(newCartItem);
        return CartItemDto.from(newOrUpdatedCartItem);
    }

    @Override
    public List<CartItemDto> getCustomerCart(Long customerId) {
        return CartItemDto.from(customerRepository.findById(customerId).orElseThrow().getCart());
    }

    @Override
    public CartItemDto deleteProductFromCart(Long customerId, Long productId) {
        CartItem foundCartItem = cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId).orElseThrow();
        cartRepository.delete(foundCartItem);
        return new CartItemDto();
    }


}
