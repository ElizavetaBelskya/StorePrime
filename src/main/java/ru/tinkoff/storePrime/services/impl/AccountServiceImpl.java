package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.exceptions.not_found.SellerNotFoundException;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.repository.SellerRepository;
import ru.tinkoff.storePrime.services.AccountService;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final CacheManager cacheManager;

    private final CustomerRepository customerRepository;

    private final SellerRepository sellerRepository;

    @Override
    public boolean isEmailUsed(String email) {
        return customerRepository.findByEmail(email).isPresent() || sellerRepository.findByEmail(email).isPresent();
    }

    @Override
    public Account getUserByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            Objects.requireNonNull(cacheManager.getCache("account")).put(customer.get().getId(), customer.get());
            return customer.get();
        }

        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if (seller.isPresent()) {
            Objects.requireNonNull(cacheManager.getCache("account")).put(seller.get().getId(), seller.get());
            return seller.get();
        }


        throw new UsernameNotFoundException("Account with such email does not exist");
    }

}
