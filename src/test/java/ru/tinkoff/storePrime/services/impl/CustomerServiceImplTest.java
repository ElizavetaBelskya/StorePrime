package ru.tinkoff.storePrime.services.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tinkoff.storePrime.converters.AddressConverter;
import ru.tinkoff.storePrime.converters.CustomerConverter;
import ru.tinkoff.storePrime.dto.location.AddressDto;
import ru.tinkoff.storePrime.dto.location.LocationDto;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.services.AccountService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountService accountService;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

   @Nested
   @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
   @DisplayName("addCustomer() is working")
   public class addCustomerTest {

       @Test
       @DisplayName("Should throw an exception when the email is already used")
       void add_customer_when_email_is_already_used() {
           NewOrUpdateCustomerDto customerDto = NewOrUpdateCustomerDto.builder()
                   .email("example@mail.ru")
                   .phoneNumber("899999999")
                   .name("Иван")
                   .surname("Иванов")
                   .gender(Customer.Gender.MALE)
                   .birthdayDate(LocalDate.of(2000, 1, 1))
                   .addressDto(AddressDto.builder()
                           .street("Спартаковская")
                           .house(98)
                           .apartment("6А")
                           .build())
                   .passwordHash("password123")
                   .build();

           when(accountService.isEmailUsed(customerDto.getEmail())).thenReturn(true);

           assertThrows(AlreadyExistsException.class, () -> {
               customerService.addCustomer(customerDto);
           });

           verify(accountService, times(1)).isEmailUsed(customerDto.getEmail());
           verifyNoMoreInteractions(accountService);
           verifyNoInteractions(passwordEncoder);
           verifyNoInteractions(customerRepository);
       }

       @Test
       @DisplayName("Should encode the password of the new customer")
       void add_customer_should_encode_password() {
           NewOrUpdateCustomerDto customerDto = NewOrUpdateCustomerDto.builder()
                   .email("example@mail.ru")
                   .phoneNumber("899999999")
                   .name("Иван")
                   .surname("Иванов")
                   .gender(Customer.Gender.MALE)
                   .birthdayDate(LocalDate.of(2000, 1, 1))
                   .addressDto(AddressDto.builder().location(
                           LocationDto.builder().country("Россия").city("Москва").build())
                           .street("Спартаковская")
                           .house(98)
                           .apartment("6А")
                           .build())
                   .passwordHash("password")
                   .build();

           when(accountService.isEmailUsed(customerDto.getEmail())).thenReturn(false);
           when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
           Customer expectedCustomer = Customer.builder()
                   .id(1L)
                   .email(customerDto.getEmail())
                   .role(Account.Role.CUSTOMER)
                   .passwordHash("encodedPassword")
                   .phoneNumber(customerDto.getPhoneNumber())
                   .state(Account.State.NOT_CONFIRMED)
                   .name(customerDto.getName())
                   .surname(customerDto.getSurname())
                   .gender(customerDto.getGender())
                   .birthdayDate(customerDto.getBirthdayDate())
                   .address(AddressConverter.getAddressFromAddressDto(customerDto.getAddressDto()))
                   .build();

           Customer newCustomer = CustomerConverter.getCustomerFromNewOrUpdateCustomerDto(customerDto);
           newCustomer.setPasswordHash("encodedPassword");
           when(customerRepository.save(newCustomer)).thenReturn(expectedCustomer);

           CustomerDto result = customerService.addCustomer(customerDto);

           assertNotNull(result);
           assertEquals(expectedCustomer.getEmail(), result.getEmail());
           assertEquals(expectedCustomer.getPhoneNumber(), result.getPhoneNumber());
           assertEquals(expectedCustomer.getName(), result.getName());
           assertEquals(expectedCustomer.getSurname(), result.getSurname());
           assertEquals(expectedCustomer.getGender(), result.getGender());
           assertEquals(expectedCustomer.getBirthdayDate(), result.getBirthdayDate());
           assertEquals(expectedCustomer.getAddress().getStreet(), result.getAddressDto().getStreet());
           assertEquals(expectedCustomer.getAddress().getHouse(), result.getAddressDto().getHouse());
           assertEquals(expectedCustomer.getAddress().getApartment(), result.getAddressDto().getApartment());

           verify(accountService, times(1)).isEmailUsed(customerDto.getEmail());
           verify(passwordEncoder, times(1)).encode("password");
           verify(customerRepository, times(1)).save(any(Customer.class));
       }

       @Test
       @DisplayName("Should add a new customer when the email is not used")
       void add_customer_success() {
           NewOrUpdateCustomerDto customerDto = NewOrUpdateCustomerDto.builder()
                   .email("example@mail.ru")
                   .phoneNumber("899999999")
                   .name("Иван")
                   .surname("Иванов")
                   .gender(Customer.Gender.MALE)
                   .birthdayDate(LocalDate.of(2000, 1, 1))
                   .addressDto(AddressDto.builder().location(LocationDto.builder().country("Россия").city("Москва").build())
                           .street("Спартаковская")
                           .house(98)
                           .apartment("6А")
                           .build())
                   .passwordHash("password123")
                   .build();

           Customer customer = Customer.builder()
                   .id(1L)
                   .role(Account.Role.CUSTOMER)
                   .state(Account.State.NOT_CONFIRMED)
                   .email(customerDto.getEmail())
                   .phoneNumber(customerDto.getPhoneNumber())
                   .name(customerDto.getName())
                   .surname(customerDto.getSurname())
                   .gender(customerDto.getGender())
                   .birthdayDate(customerDto.getBirthdayDate())
                   .address(AddressConverter.getAddressFromAddressDto(customerDto.getAddressDto()))
                   .build();

           when(accountService.isEmailUsed(customerDto.getEmail())).thenReturn(false);
           when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
           Customer newCustomer = CustomerConverter.getCustomerFromNewOrUpdateCustomerDto(customerDto);
           newCustomer.setPasswordHash("encodedPassword");
           when(customerRepository.save(newCustomer)).thenReturn(customer);

           CustomerDto result = customerService.addCustomer(customerDto);

           assertNotNull(result);
           assertEquals(customer.getEmail(), result.getEmail());
           assertEquals(customer.getPhoneNumber(), result.getPhoneNumber());
           assertEquals(customer.getName(), result.getName());
           assertEquals(customer.getSurname(), result.getSurname());
           assertEquals(customer.getGender(), result.getGender());
           assertEquals(customer.getBirthdayDate(), result.getBirthdayDate());
           assertEquals(customer.getAddress(), AddressConverter.getAddressFromAddressDto(result.getAddressDto()));

           verify(accountService, times(1)).isEmailUsed(customerDto.getEmail());
           verify(passwordEncoder, times(1)).encode("password123");
           verify(customerRepository, times(1)).save(CustomerConverter.getCustomerFromNewOrUpdateCustomerDto(customerDto));
       }

   }



}