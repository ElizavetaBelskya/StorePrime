package ru.tinkoff.storePrime.services.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.storePrime.exceptions.not_found.CustomerNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.SellerNotFoundException;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.repository.SellerRepository;


@Component
@RequiredArgsConstructor
public class AccountCachingUtil {

    private final CacheManager cacheManager;

    private final CustomerRepository customerRepository;

    private final SellerRepository sellerRepository;


    public Customer getCustomer(Long customerId) {
        try {
            Cache.ValueWrapper customerWrapper = cacheManager.getCache("account").get(customerId);
            if (customerWrapper != null && customerWrapper instanceof SimpleValueWrapper) {
                Object customerObject = customerWrapper.get();
                if (customerObject instanceof Customer) {
                    return (Customer) customerObject;
                }
            }
        } catch (NullPointerException e) {
        }
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Покупатель с id " + customerId + " не найден"));
    }



    public Seller getSeller(Long sellerId) {
        try {
            Cache.ValueWrapper customerWrapper = cacheManager.getCache("account").get(sellerId);
            if (customerWrapper != null && customerWrapper instanceof SimpleValueWrapper) {
                Object sellerObject = customerWrapper.get();
                if (sellerObject instanceof Customer) {
                    return (Seller) sellerObject;
                }
            }
        } catch (NullPointerException e) {
        }

        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Продавец с id " + sellerId + " не найден"));
    }



}
