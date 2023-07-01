package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.SellerDto;

public interface SellerService {

    SellerDto addSeller(NewOrUpdateSellerDto sellerDto);

}
