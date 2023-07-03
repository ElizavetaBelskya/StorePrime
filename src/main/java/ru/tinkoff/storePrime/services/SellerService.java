package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.user.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.user.SellerDto;

public interface SellerService {

    SellerDto addSeller(NewOrUpdateSellerDto sellerDto);

    SellerDto deleteSeller(Long sellerId);

    SellerDto updateSeller(Long id, NewOrUpdateSellerDto updatedSeller);

    SellerDto getSeller(Long id);
}
