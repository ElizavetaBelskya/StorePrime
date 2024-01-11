package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.SellerApi;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.user.SellerDto;
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
        return ResponseEntity.ok(sellerService.getSeller(userDetailsImpl.getAccount().getId()));
    }

    @Override
    public ResponseEntity<SellerDto> getSellerById(Long id) {
        return ResponseEntity.ok(sellerService.getSeller(id));
    }

    @Override
    public ResponseEntity<Void> deleteSeller(UserDetailsImpl userDetailsImpl) {
        Long sellerId = userDetailsImpl.getAccount().getId();
        sellerService.deleteSeller(sellerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
