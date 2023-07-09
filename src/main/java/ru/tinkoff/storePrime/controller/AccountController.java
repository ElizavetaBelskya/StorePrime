package ru.tinkoff.storePrime.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.AccountApi;
import ru.tinkoff.storePrime.converters.CustomerConverter;
import ru.tinkoff.storePrime.converters.SellerConverter;
import ru.tinkoff.storePrime.dto.user.AccountDto;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.SellerDto;
import ru.tinkoff.storePrime.exceptions.ForbiddenException;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

@RestController
public class AccountController implements AccountApi {

    @Override
    public ResponseEntity<AccountDto> getThisAccount(UserDetailsImpl userDetailsImpl) {
        Account account = userDetailsImpl.getAccount();
        if (Account.Role.CUSTOMER.equals(account.getRole())) {
            return ResponseEntity.ok(CustomerConverter.getCustomerDtoFromCustomer((Customer) account));
        } else if (Account.Role.SELLER.equals(account.getRole())) {
            return ResponseEntity.ok(SellerConverter.getSellerDtoFromSeller((Seller) account));
        } else {
            throw new ForbiddenException("Запрет доступа");
        }
    }


}
