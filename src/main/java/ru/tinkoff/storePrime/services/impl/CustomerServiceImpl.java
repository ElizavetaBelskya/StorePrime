package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.config.CacheConfig;
import ru.tinkoff.storePrime.converters.CustomerConverter;
import ru.tinkoff.storePrime.converters.SellerConverter;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.exceptions.PaymentImpossibleException;
import ru.tinkoff.storePrime.exceptions.not_found.CustomerNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.SellerNotFoundException;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.services.AccountService;
import ru.tinkoff.storePrime.services.CustomerService;
import ru.tinkoff.storePrime.services.utils.AccountCachingUtil;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {


    private final CacheManager cacheManager;

    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    private final CustomerRepository customerRepository;

    private AccountCachingUtil accountCachingUtil;

    @Autowired
    public void setAccountCachingUtil(AccountCachingUtil accountCachingUtil) {
        this.accountCachingUtil = accountCachingUtil;
    }

    @Override
    public CustomerDto addCustomer(NewOrUpdateCustomerDto customerDto) {
        if (accountService.isEmailUsed(customerDto.getEmail())) {
            throw new AlreadyExistsException("Account with email <" + customerDto.getEmail() + "> already exists");
        }
        customerDto.setPasswordHash(passwordEncoder.encode(customerDto.getPasswordHash()));
        Customer customer = CustomerConverter.getCustomerFromNewOrUpdateCustomerDto(customerDto);
        customer.setCardBalance(0.0);
        customer = customerRepository.save(customer);
        return CustomerConverter.getCustomerDtoFromCustomer(customer);
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, NewOrUpdateCustomerDto updatedCustomerDto) {
        Customer customer = accountCachingUtil.getCustomer(customerId);
        if (Account.State.DELETED.equals(customer.getState())) {
            throw new CustomerNotFoundException("Пользователь не найден");
        }
        if (!updatedCustomerDto.getEmail().equals(customer.getEmail())) {
            if (accountService.isEmailUsed(updatedCustomerDto.getEmail())) {
                throw new AlreadyExistsException("Account with email <" + updatedCustomerDto.getEmail() + "> already exists");
            }
        }
        Customer updateCustomer = CustomerConverter.getCustomerFromNewOrUpdateCustomerDto(updatedCustomerDto);
        updateCustomer.setPasswordHash(passwordEncoder.encode(updatedCustomerDto.getPasswordHash()));
        updateCustomer.setCardBalance(customer.getCardBalance());
        updateCustomer.setCart(customer.getCart());
        updateCustomer.setId(customerId);
        Customer updatedCustomer = customerRepository.save(updateCustomer);
        if (cacheManager.getCache("account") != null) {
            cacheManager.getCache("account").put(updatedCustomer.getId(), updatedCustomer);
        }
        return CustomerConverter.getCustomerDtoFromCustomer(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = accountCachingUtil.getCustomer(customerId);
        customer.setState(Account.State.DELETED);
        customerRepository.save(customer);
        if (cacheManager.getCache("account") != null) {
            cacheManager.getCache("account").invalidate();
        }
    }

    @Override
    public CustomerDto updateCardBalance(Long customerId, Double replenishment) {
        Customer customer = accountCachingUtil.getCustomer(customerId);
        if (customer.getCardBalance() + replenishment < 0) {
            throw new PaymentImpossibleException("Недостаточно средств");
        }
        customer.setCardBalance(customer.getCardBalance() + replenishment);
        Customer updatedCustomer = customerRepository.save(customer);
        if (cacheManager.getCache("account") != null) {
            cacheManager.getCache("account").put(customerId, updatedCustomer);
        }
        return CustomerConverter.getCustomerDtoFromCustomer(updatedCustomer);
    }

    @Override
    public CustomerDto getThisCustomer(Long id) {
        Customer customer = accountCachingUtil.getCustomer(id);
        if (Account.State.DELETED.equals(customer.getState())) {
            throw new CustomerNotFoundException("Пользователь не найден");
        }
        return CustomerConverter.getCustomerDtoFromCustomer(customer);
    }


}
