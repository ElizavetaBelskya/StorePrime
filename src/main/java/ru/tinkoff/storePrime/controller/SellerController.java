package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.SellerApi;
import ru.tinkoff.storePrime.dto.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.SellerDto;
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


}
