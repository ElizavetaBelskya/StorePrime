package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.converters.SellerConverter;
import ru.tinkoff.storePrime.dto.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.SellerDto;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.SellerRepository;
import ru.tinkoff.storePrime.services.SellerService;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    @Override
    public SellerDto addSeller(NewOrUpdateSellerDto sellerDto) {
        Seller newSeller = SellerConverter.getSellerFromNewOrUpdateSellerDto(sellerDto);
        Seller seller = sellerRepository.save(newSeller);
        return SellerDto.from(seller);
    }


//    public boolean isEmailUsed(String email) {
//        return sellerRepository.findByEmail(email).isPresent();
//    }

}
