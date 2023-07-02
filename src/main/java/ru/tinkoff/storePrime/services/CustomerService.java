package ru.tinkoff.storePrime.services;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ru.tinkoff.storePrime.dto.CustomerDto;
import ru.tinkoff.storePrime.dto.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

public interface CustomerService {
    CustomerDto addCustomer(NewOrUpdateCustomerDto newCustomer);

    CustomerDto updateCustomer(Long id, NewOrUpdateCustomerDto updatedCustomerDto);

    CustomerDto deleteCustomer(Long id);

    CustomerDto updateCardBalance(Long customerId, Integer replenishment);
}
