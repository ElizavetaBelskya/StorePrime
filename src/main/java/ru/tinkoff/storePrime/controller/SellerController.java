package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.SellerApi;
import ru.tinkoff.storePrime.dto.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.SellerDto;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.services.AccountService;
import ru.tinkoff.storePrime.services.SellerService;

@RequiredArgsConstructor
@RestController
public class SellerController implements SellerApi {

    private final AccountService accountService;

    private final SellerService sellerService;


    @Override
    public ResponseEntity<SellerDto> addSeller(NewOrUpdateSellerDto newSeller) {
        if (accountService.isEmailUsed(newSeller.getEmail())) {
            throw new AlreadyExistsException("Account with email <" + newSeller.getEmail() + "> already exists");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.addSeller(newSeller));
        }
    }

    @Override
    public ResponseEntity<SellerDto> updateSeller(UserDetailsImpl userDetailsImpl, NewOrUpdateSellerDto updatedSeller) {
        Long sellerId = userDetailsImpl.getAccount().getId();
        SellerDto sellerDto = sellerService.updateSeller(sellerId, updatedSeller);
        return ResponseEntity.accepted().body(sellerDto);
    }

    @Override
    public ResponseEntity<SellerDto> getThisSeller(UserDetailsImpl userDetailsImpl) {
        return ResponseEntity.ok(SellerDto.from((Seller) userDetailsImpl.getAccount()));
    }

    @Override
    public ResponseEntity<SellerDto> getSellerById(Long id) {
        return ResponseEntity.ok(sellerService.getSeller(id));
    }

    @Override
    public ResponseEntity<SellerDto> deleteSeller(UserDetailsImpl userDetailsImpl) {
        SellerDto deletedSellerDto = sellerService.deleteSeller(userDetailsImpl.getAccount().getId());
        return ResponseEntity.accepted().body(deletedSellerDto);
    }


}
