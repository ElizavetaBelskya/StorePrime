package ru.tinkoff.storePrime.converters;

import ru.tinkoff.storePrime.dto.location.AddressDto;
import ru.tinkoff.storePrime.models.Address;
import ru.tinkoff.storePrime.models.Location;

public class AddressConverter {

    public static Address getAddressFromAddressDto(AddressDto addressDto) {
        return Address.builder().location(new Location(
                addressDto.getLocation().getCountry(),
                addressDto.getLocation().getCity()))
                .street(addressDto.getStreet())
                .house(addressDto.getHouse())
                .apartment(addressDto.getApartment())
                .build();
    }


}
