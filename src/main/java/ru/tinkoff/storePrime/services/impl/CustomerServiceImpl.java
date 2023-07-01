package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.converters.AddressConverter;
import ru.tinkoff.storePrime.converters.CustomerConverter;
import ru.tinkoff.storePrime.dto.CustomerDto;
import ru.tinkoff.storePrime.dto.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.services.AccountService;
import ru.tinkoff.storePrime.services.CustomerService;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final AccountService accountService;

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto addCustomer(NewOrUpdateCustomerDto customerDto) {
        if (accountService.isEmailUsed(customerDto.getEmail())) {
            throw new AlreadyExistsException("Account with email <" + customerDto.getEmail() + "> already exists");
        }
        Customer newCustomer = CustomerConverter.getCustomerFromNewOrUpdateCustomerDto(customerDto);
        Customer customer = customerRepository.save(newCustomer);
        return CustomerDto.from(customer);
    }

    @Override
    public CustomerDto updateCustomer(Long id, NewOrUpdateCustomerDto updatedCustomerDto) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        if (!updatedCustomerDto.getEmail().equals(customer.getEmail())) {
            if (accountService.isEmailUsed(updatedCustomerDto.getEmail())) {
                throw new AlreadyExistsException("Account with email <" + updatedCustomerDto.getEmail() + "> already exists");
            } else {
                customer.setEmail(updatedCustomerDto.getEmail());
            }
        }
        customer.setPhoneNumber(updatedCustomerDto.getPhoneNumber());
        customer.setName(updatedCustomerDto.getName());
        customer.setSurname(updatedCustomerDto.getSurname());
        customer.setGender(updatedCustomerDto.getGender());
        customer.setBirthdayDate(updatedCustomerDto.getBirthdayDate());
        customer.setPasswordHash(updatedCustomerDto.getPasswordHash());
        customer.setAddress(AddressConverter.getAddressFromAddressDto(updatedCustomerDto.getAddressDto()));
        Customer updatedCustomer = customerRepository.save(customer);
        return CustomerDto.from(updatedCustomer);
    }

}
