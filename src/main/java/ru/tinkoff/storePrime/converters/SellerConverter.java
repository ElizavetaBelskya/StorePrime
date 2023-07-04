package ru.tinkoff.storePrime.converters;

import ru.tinkoff.storePrime.dto.user.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.models.Location;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Seller;

public class SellerConverter {

    private SellerConverter(){};
    public static Seller getSellerFromNewOrUpdateSellerDto(NewOrUpdateSellerDto sellerDto) {
        return Seller.builder()
                .description(sellerDto.getDescription())
                .location(new Location(sellerDto.getLocationDto().getCountry(), sellerDto.getLocationDto().getCity()))
                .name(sellerDto.getName())
                .INN(sellerDto.getINN())
                .email(sellerDto.getEmail())
                .role(Account.Role.SELLER)
                .state(Account.State.NOT_CONFIRMED)
                .phoneNumber(sellerDto.getPhoneNumber())
                .cardBalance(0L).build();
    }



}
