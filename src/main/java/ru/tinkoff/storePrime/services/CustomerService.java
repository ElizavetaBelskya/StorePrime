package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.CustomerDto;
import ru.tinkoff.storePrime.dto.NewOrUpdateCustomerDto;

public interface CustomerService {
    CustomerDto addCustomer(NewOrUpdateCustomerDto newCustomer);

    CustomerDto updateCustomer(Long id, NewOrUpdateCustomerDto updatedCustomerDto);
}
