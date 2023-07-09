package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.converters.SellerConverter;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.user.SellerDto;
import ru.tinkoff.storePrime.exceptions.PaymentImpossibleException;
import ru.tinkoff.storePrime.exceptions.not_found.SellerNotFoundException;
import ru.tinkoff.storePrime.models.user.Account;
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

    private final PasswordEncoder passwordEncoder;

    @Override
    public SellerDto addSeller(NewOrUpdateSellerDto sellerDto) {
        Seller newSeller = SellerConverter.getSellerFromNewOrUpdateSellerDto(sellerDto);
        Seller seller = sellerRepository.save(newSeller);
        return SellerConverter.getSellerDtoFromSeller(seller);
    }

    @Override
    public void deleteSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new SellerNotFoundException("Продавец с id " + sellerId + " не найден"));
        seller.setState(Account.State.DELETED);
    }

    @Override
    public SellerDto updateSeller(Long sellerId, NewOrUpdateSellerDto updatedSellerDto) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() ->
                new SellerNotFoundException("Продавец с id " + sellerId + " не найден"));
        if (!updatedSellerDto.getEmail().equals(seller.getEmail())) {
            if (accountService.isEmailUsed(updatedSellerDto.getEmail())) {
                throw new AlreadyExistsException("Account with email <" + updatedSellerDto.getEmail() + "> already exists");
            }
        }
        if (Account.State.DELETED.equals(seller.getState())) {
            throw new SellerNotFoundException("Продавец с id " + sellerId + " удален");
        }
        seller = SellerConverter.getSellerFromNewOrUpdateSellerDto(updatedSellerDto);
        seller.setPasswordHash(passwordEncoder.encode(seller.getPasswordHash()));
        Seller updatedSeller = sellerRepository.save(seller);
        return SellerConverter.getSellerDtoFromSeller(updatedSeller);
    }

    @Override
    public SellerDto getSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() ->
                new SellerNotFoundException("Продавец с id " + sellerId + " не найден"));
        if (Account.State.DELETED.equals(seller.getState())) {
            throw new SellerNotFoundException("Пользователь не найден");
        }
        return SellerConverter.getSellerDtoFromSeller(seller);
    }

    @Override
    public void updateCardBalanceBySellerId(Long sellerId, Double replenishment) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() ->
                new SellerNotFoundException("Продавец с id " + sellerId + " не найден"));
        if (seller.getCardBalance() + replenishment < 0) {
            throw new PaymentImpossibleException("Недостаточно средств");
        }
        seller.setCardBalance(seller.getCardBalance() + replenishment);
        sellerRepository.save(seller);
    }


}
