package ru.tinkoff.storePrime.services.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.storePrime.exceptions.not_found.CustomerNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.SellerNotFoundException;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.repository.SellerRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AccountCachingUtil {

    private final CacheManager cacheManager;

    private final CustomerRepository customerRepository;

    private final SellerRepository sellerRepository;


    public Customer getCustomer(Long customerId) {
        Object customerWrapper = Objects.requireNonNull(cacheManager.getCache("account")).get(customerId);

        if (customerWrapper instanceof SimpleValueWrapper) {
            Object customerObject = ((SimpleValueWrapper) customerWrapper).get();
            if (customerObject instanceof Customer) {
                return (Customer) customerObject;
            }
        }
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Покупатель с id " + customerId + " не найден"));
    }

    public Seller getSeller(Long sellerId) {
        Object customerWrapper = Objects.requireNonNull(cacheManager.getCache("account")).get(sellerId);

        if (customerWrapper instanceof SimpleValueWrapper) {
            Object customerObject = ((SimpleValueWrapper) customerWrapper).get();
            if (customerObject instanceof Seller) {
                return (Seller) customerObject;
            }
        }
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Продавец с id " + sellerId + " не найден"));
    }


}
