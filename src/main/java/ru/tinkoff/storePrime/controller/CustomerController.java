package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.CustomerApi;
import ru.tinkoff.storePrime.dto.CustomerDto;
import ru.tinkoff.storePrime.dto.NewOrUpdateCustomerDto;
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
    public ResponseEntity<CustomerDto> updateCustomer(Long customerId, NewOrUpdateCustomerDto updatedCustomerDto) {
        return ResponseEntity.accepted().body(customerService.updateCustomer(customerId, updatedCustomerDto));
    }

    @Override
    public ResponseEntity<CustomerDto> getThisCustomer(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ResponseEntity.ok(CustomerDto.from((Customer) userDetailsImpl.getAccount()));
    }

    @Override
    public ResponseEntity<CustomerDto> deleteCustomer(UserDetailsImpl userDetailsImpl) {
        CustomerDto deletedCustomerDto = customerService.deleteCustomer((Customer) userDetailsImpl.getAccount());
        return ResponseEntity.accepted().body(deletedCustomerDto);
    }


}
