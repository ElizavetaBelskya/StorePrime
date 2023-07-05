package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.converters.CustomerConverter;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.exceptions.NotFoundException;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.services.AccountService;
import ru.tinkoff.storePrime.services.CustomerService;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto addCustomer(NewOrUpdateCustomerDto customerDto) {
        if (accountService.isEmailUsed(customerDto.getEmail())) {
            throw new AlreadyExistsException("Account with email <" + customerDto.getEmail() + "> already exists");
        }
        customerDto.setPasswordHash(passwordEncoder.encode(customerDto.getPasswordHash()));
        Customer customer = CustomerConverter.getCustomerFromNewOrUpdateCustomerDto(customerDto);
        customer = customerRepository.save(customer);
        return CustomerDto.from(customer);
    }

    @Override
    public CustomerDto updateCustomer(Long id, NewOrUpdateCustomerDto updatedCustomerDto) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        if (Account.State.DELETED.equals(customer.getState())) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!updatedCustomerDto.getEmail().equals(customer.getEmail())) {
            if (accountService.isEmailUsed(updatedCustomerDto.getEmail())) {
                throw new AlreadyExistsException("Account with email <" + updatedCustomerDto.getEmail() + "> already exists");
            }
        }
        customer = CustomerConverter.getCustomerFromNewOrUpdateCustomerDto(updatedCustomerDto);
        customer.setPasswordHash(passwordEncoder.encode(updatedCustomerDto.getPasswordHash()));
        Customer updatedCustomer = customerRepository.save(customer);
        return CustomerDto.from(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        customer.setState(Account.State.DELETED);
        customerRepository.save(customer);
    }

    @Override
    public CustomerDto updateCardBalance(Long customerId, Double replenishment) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        customer.setCardBalance(customer.getCardBalance() + replenishment);
        return CustomerDto.from(customerRepository.save(customer));
    }

}
