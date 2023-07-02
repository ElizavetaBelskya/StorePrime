package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.converters.SellerConverter;
import ru.tinkoff.storePrime.dto.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.SellerDto;
import ru.tinkoff.storePrime.models.Location;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.SellerRepository;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.services.AccountService;
import ru.tinkoff.storePrime.services.SellerService;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    private final AccountService accountService;

    @Override
    public SellerDto addSeller(NewOrUpdateSellerDto sellerDto) {
        Seller newSeller = SellerConverter.getSellerFromNewOrUpdateSellerDto(sellerDto);
        Seller seller = sellerRepository.save(newSeller);
        return SellerDto.from(seller);
    }

    @Override
    public SellerDto deleteSeller(Seller seller) {
        sellerRepository.delete(seller);
        return new SellerDto();
    }

    @Override
    public SellerDto updateSeller(Long id, NewOrUpdateSellerDto updatedSellerDto) {
        Seller seller = sellerRepository.findById(id).orElseThrow();
        if (!updatedSellerDto.getEmail().equals(seller.getEmail())) {
            if (accountService.isEmailUsed(updatedSellerDto.getEmail())) {
                throw new AlreadyExistsException("Account with email <" + updatedSellerDto.getEmail() + "> already exists");
            } else {
                seller.setEmail(updatedSellerDto.getEmail());
            }
        }
        seller.setPhoneNumber(updatedSellerDto.getPhoneNumber());
        seller.setName(updatedSellerDto.getName());
        seller.setINN(updatedSellerDto.getINN());
        seller.setLocation(new Location(updatedSellerDto.getLocationDto().getCountry(),
                updatedSellerDto.getLocationDto().getCity()));
        seller.setDescription(updatedSellerDto.getDescription());
        Seller updatedSeller = sellerRepository.save(seller);
        return SellerDto.from(updatedSeller);
    }


//    public boolean isEmailUsed(String email) {
//        return sellerRepository.findByEmail(email).isPresent();
//    }

}
