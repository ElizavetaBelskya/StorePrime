package ru.tinkoff.storePrime.converters;

import ru.tinkoff.storePrime.dto.location.AddressDto;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;

public class CustomerConverter {

    private CustomerConverter(){}
    public static Customer getCustomerFromNewOrUpdateCustomerDto(NewOrUpdateCustomerDto customerDto) {
        return Customer
                .builder()
                .email(customerDto.getEmail())
                .role(Account.Role.CUSTOMER)
                .passwordHash(customerDto.getPasswordHash())
                .phoneNumber(customerDto.getPhoneNumber())
                .state(Account.State.NOT_CONFIRMED)
                .name(customerDto.getName())
                .surname(customerDto.getSurname())
                .birthdayDate(customerDto.getBirthdayDate())
                .gender(customerDto.getGender())
                .address(AddressConverter.getAddressFromAddressDto(customerDto.getAddressDto()))
                .build();
    }

    public static CustomerDto getCustomerDtoFromCustomer(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .cardBalance(customer.getCardBalance())
                .name(customer.getName())
                .surname(customer.getSurname())
                .gender(customer.getGender())
                .birthdayDate(customer.getBirthdayDate().toString())
                .addressDto(AddressConverter.getAddressDtoFromAddress(customer.getAddress()))
                .build();
    }



}
