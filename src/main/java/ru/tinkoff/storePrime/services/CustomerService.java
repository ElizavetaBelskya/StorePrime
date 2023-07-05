package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;

public interface CustomerService {
    CustomerDto addCustomer(NewOrUpdateCustomerDto newCustomer);

    CustomerDto updateCustomer(Long id, NewOrUpdateCustomerDto updatedCustomerDto);

    void deleteCustomer(Long id);

    CustomerDto updateCardBalance(Long customerId, Double replenishment);

}
