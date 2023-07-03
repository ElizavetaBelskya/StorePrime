package ru.tinkoff.storePrime.converters;

import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;

public class CustomerConverter {

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


}
