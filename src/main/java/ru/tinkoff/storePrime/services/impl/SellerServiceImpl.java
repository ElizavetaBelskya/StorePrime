package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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
import ru.tinkoff.storePrime.services.utils.AccountCachingUtil;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    private final CacheManager cacheManager;

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    private AccountCachingUtil accountCachingUtil;

    @Autowired
    public void setAccountCachingUtil(AccountCachingUtil accountCachingUtil) {
        this.accountCachingUtil = accountCachingUtil;
    }

    @Override
    public SellerDto addSeller(NewOrUpdateSellerDto sellerDto) {
        Seller newSeller = SellerConverter.getSellerFromNewOrUpdateSellerDto(sellerDto);
        newSeller.setPasswordHash(passwordEncoder.encode(newSeller.getPasswordHash()));
        newSeller.setCardBalance(0.0);
        Seller seller = sellerRepository.save(newSeller);
        return SellerConverter.getSellerDtoFromSeller(seller);
    }

    @Override
    public void deleteSeller(Long sellerId) {
        Seller seller = accountCachingUtil.getSeller(sellerId);
        seller.setState(Account.State.DELETED);
        sellerRepository.save(seller);
        if (cacheManager.getCache("account") != null) {
            cacheManager.getCache("account").invalidate();
        }
    }

    @Override
    public SellerDto updateSeller(Long sellerId, NewOrUpdateSellerDto updatedSellerDto) {
        Seller seller = accountCachingUtil.getSeller(sellerId);
        if (!updatedSellerDto.getEmail().equals(seller.getEmail())) {
            if (accountService.isEmailUsed(updatedSellerDto.getEmail())) {
                throw new AlreadyExistsException("Account with email <" + updatedSellerDto.getEmail() + "> already exists");
            }
        }
        if (Account.State.DELETED.equals(seller.getState())) {
            throw new SellerNotFoundException("Продавец с id " + sellerId + " удален");
        }
        Seller newSeller = SellerConverter.getSellerFromNewOrUpdateSellerDto(updatedSellerDto);
        newSeller.setPasswordHash(passwordEncoder.encode(seller.getPasswordHash()));
        newSeller.setId(sellerId);
        newSeller.setCardBalance(seller.getCardBalance());
        Seller updatedSeller = sellerRepository.save(newSeller);
        if (cacheManager.getCache("account") != null) {
            Objects.requireNonNull(cacheManager.getCache("account")).put(updatedSeller.getId(), updatedSeller);
        }
        return SellerConverter.getSellerDtoFromSeller(updatedSeller);
    }

    @Override
    public SellerDto getSeller(Long sellerId) {
        Seller seller = accountCachingUtil.getSeller(sellerId);
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
        Seller updatedSeller = sellerRepository.save(seller);
        if (cacheManager.getCache("account") != null) {
            cacheManager.getCache("account").put(updatedSeller.getId(), updatedSeller);
        }
    }


}
