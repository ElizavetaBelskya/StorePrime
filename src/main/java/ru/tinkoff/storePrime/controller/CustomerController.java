package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.CustomerApi;
import ru.tinkoff.storePrime.converters.CustomerConverter;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.services.CustomerService;

@RequiredArgsConstructor
@RestController
public class CustomerController implements CustomerApi {

    private final CustomerService customerService;

    @Override
    public ResponseEntity<CustomerDto> addCustomer(NewOrUpdateCustomerDto newCustomer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addCustomer(newCustomer));
    }

    @Override
    public ResponseEntity<CustomerDto> updateCustomerCardBalance(UserDetailsImpl userDetailsImpl, NewOrUpdateCustomerDto updatedCustomerDto) {
        Long customerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.accepted().body(customerService.updateCustomer(customerId, updatedCustomerDto));
    }

    @Override
    public ResponseEntity<CustomerDto> getThisCustomer(UserDetailsImpl userDetailsImpl) {
        return ResponseEntity.ok(CustomerConverter.getCustomerDtoFromCustomer((Customer) userDetailsImpl.getAccount()));
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(UserDetailsImpl userDetailsImpl) {
        customerService.deleteCustomer(userDetailsImpl.getAccount().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<CustomerDto> updateCustomerCardBalance(UserDetailsImpl userDetailsImpl, Double replenishment) {
        Long customerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(customerService.updateCardBalance(customerId, replenishment));
    }


}
