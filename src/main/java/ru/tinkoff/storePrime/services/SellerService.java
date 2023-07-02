package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.SellerDto;
import ru.tinkoff.storePrime.models.user.Seller;

public interface SellerService {

    SellerDto addSeller(NewOrUpdateSellerDto sellerDto);

    SellerDto deleteSeller(Long sellerId);

    SellerDto updateSeller(Long id, NewOrUpdateSellerDto updatedSeller);

    SellerDto getSeller(Long id);
}
