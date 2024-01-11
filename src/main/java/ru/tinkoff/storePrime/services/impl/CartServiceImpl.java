package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.dto.cart.CartItemDto;
import ru.tinkoff.storePrime.exceptions.not_found.CartItemNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.ProductNotFoundException;
import ru.tinkoff.storePrime.models.CartItem;
import ru.tinkoff.storePrime.models.Product;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.repository.CartRepository;
import ru.tinkoff.storePrime.repository.ProductRepository;
import ru.tinkoff.storePrime.services.CartService;
import ru.tinkoff.storePrime.services.utils.AccountCachingUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private AccountCachingUtil accountCachingUtil;

    @Autowired
    public void setAccountCachingUtil(AccountCachingUtil accountCachingUtil) {
        this.accountCachingUtil = accountCachingUtil;
    }

    @Override
    public CartItemDto addNewCartItem(Long customerId, Long productId, Integer quantity) {
        Optional<CartItem> foundCartItem = cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId);
        CartItem newCartItem = foundCartItem.orElseGet(() -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));
            Customer customer = accountCachingUtil.getCustomer(customerId);
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
        return CartItemDto.from(cartRepository.findByCustomer_Id(customerId));
    }

    @Override
    public void deleteProductFromCart(Long customerId, Long productId) {
        CartItem foundCartItem = cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId).orElseThrow(() ->
                new CartItemNotFoundException("Элемент корзины не найден"));
        cartRepository.delete(foundCartItem);
    }


}
