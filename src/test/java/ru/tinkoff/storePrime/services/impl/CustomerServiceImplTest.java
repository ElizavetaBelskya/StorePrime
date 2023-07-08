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
import ru.tinkoff.storePrime.exceptions.PaymentImpossibleException;
import ru.tinkoff.storePrime.exceptions.not_found.CustomerNotFoundException;
import ru.tinkoff.storePrime.models.Address;
import ru.tinkoff.storePrime.models.Location;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.services.AccountService;

import java.time.LocalDate;
import java.util.Optional;

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

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("updateCustomer() is working")
    public class updateCustomerTest {

        @Test
        @DisplayName("Should throw an exception when the customer is not found")
        void update_customer_when_customer_not_found() {
            Long customerId = 1L;
            NewOrUpdateCustomerDto updatedCustomerDto = NewOrUpdateCustomerDto.builder()
                    .email("newemail@mail.ru")
                    .phoneNumber("888888888")
                    .name("Updated")
                    .surname("Customer")
                    .gender(Customer.Gender.FEMALE)
                    .birthdayDate(LocalDate.of(1990, 5, 15))
                    .addressDto(AddressDto.builder()
                            .street("Новая улица")
                            .house(100)
                            .apartment("10")
                            .build())
                    .passwordHash("newpassword")
                    .build();

            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class, () -> {
                customerService.updateCustomer(customerId, updatedCustomerDto);
            });

            verify(customerRepository, times(1)).findById(customerId);
            verifyNoMoreInteractions(customerRepository);
            verifyNoInteractions(passwordEncoder);
            verifyNoInteractions(accountService);
        }

        @Test
        @DisplayName("Should throw an exception when the customer state is deleted")
        void update_customer_when_customer_state_is_deleted() {
            Long id = 1L;
            NewOrUpdateCustomerDto updatedCustomerDto = NewOrUpdateCustomerDto.builder()
                    .email("newemail@mail.ru")
                    .phoneNumber("888888888")
                    .name("Updated")
                    .surname("Customer")
                    .gender(Customer.Gender.FEMALE)
                    .birthdayDate(LocalDate.of(1990, 5, 15))
                    .addressDto(AddressDto.builder()
                            .street("Новая улица")
                            .house(100)
                            .apartment("10")
                            .build())
                    .passwordHash("newpassword")
                    .build();

            Customer customer = Customer.builder()
                    .id(id)
                    .role(Account.Role.CUSTOMER)
                    .state(Account.State.DELETED)
                    .email("oldemail@mail.ru")
                    .phoneNumber("999999999")
                    .name("Old")
                    .surname("Customer")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.of(1990, 1, 1))
                    .address(Address.builder()
                            .location(Location.builder().country("Россия").city("Москва").build())
                            .street("Старая улица")
                            .house(50)
                            .apartment("5")
                            .build())
                    .build();

            when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

            assertThrows(CustomerNotFoundException.class, () -> {
                customerService.updateCustomer(id, updatedCustomerDto);
            });

            verify(customerRepository, times(1)).findById(id);
            verifyNoMoreInteractions(customerRepository);
            verifyNoInteractions(accountService);
            verifyNoInteractions(passwordEncoder);
        }

        @Test
        @DisplayName("Should throw an exception when the email is already used")
        void update_customer_when_email_is_already_used() {
            Long id = 1L;
            NewOrUpdateCustomerDto updatedCustomerDto = NewOrUpdateCustomerDto.builder()
                    .email("newemail@mail.ru")
                    .phoneNumber("899999999")
                    .name("Иван")
                    .surname("Иванов")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.of(2000, 1, 1))
                    .addressDto(AddressDto.builder().location(
                                    LocationDto.builder().country("Россия").city("Москва").build())
                            .street("Спартаковская")
                            .house(98)
                            .apartment("6А").build())
                    .passwordHash("password123")
                    .build();

            Customer existingCustomer = Customer.builder()
                    .id(id)
                    .role(Account.Role.CUSTOMER)
                    .state(Account.State.CONFIRMED)
                    .email("existingemail@mail.ru")
                    .phoneNumber("899999999")
                    .name("Иван")
                    .surname("Иванов")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.of(2000, 1, 1))
                    .address(Address.builder()
                            .location(Location.builder().country("Россия").city("Москва").build())
                            .street("Спартаковская")
                            .house(98)
                            .apartment("6А")
                            .build())
                    .build();

            when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
            when(accountService.isEmailUsed(updatedCustomerDto.getEmail())).thenReturn(true);

            assertThrows(AlreadyExistsException.class, () -> {
                customerService.updateCustomer(id, updatedCustomerDto);
            });

            verify(customerRepository, times(1)).findById(id);
            verify(accountService, times(1)).isEmailUsed(updatedCustomerDto.getEmail());
            verifyNoMoreInteractions(customerRepository);
            verifyNoMoreInteractions(accountService);
            verifyNoInteractions(passwordEncoder);
        }

    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("deleteCustomer() is working")
    public class deleteCustomerTest {
        @Test
        @DisplayName("Should throw an exception when the customer id does not exist")
        void delete_customer_when_customer_id_does_not_exist() {
            Long customerId = 1L;

            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class, () -> {
                customerService.deleteCustomer(customerId);
            });

            verify(customerRepository, times(1)).findById(customerId);
            verifyNoMoreInteractions(customerRepository);
        }

        @Test
        @DisplayName("Should mark the customer as deleted when the customer id exists")
        void delete_customer_success() {
            Long customerId = 1L;
            Customer customer = Customer.builder()
                    .id(customerId)
                    .role(Account.Role.CUSTOMER)
                    .state(Account.State.CONFIRMED)
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("John")
                    .surname("Doe")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.of(1990, 1, 1))
                    .address(Address.builder()
                            .location(Location.builder().country("Russia").city("Moscow").build())
                            .street("Spartakovskaya")
                            .house(98)
                            .apartment("6A")
                            .build())
                    .build();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

            customerService.deleteCustomer(customerId);

            assertEquals(Account.State.DELETED, customer.getState());

            verify(customerRepository, times(1)).findById(customerId);
            verify(customerRepository, times(1)).save(customer);
            verifyNoMoreInteractions(customerRepository);
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("updateCardBalance() is working")
    public class updateCardBalanceTest {

        @Test
        @DisplayName("Should throw an exception when the customer is not found")
        void update_card_balance_when_customer_not_found() {
            Long customerId = 1L;
            Double replenishment = 100.0;

            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class, () -> {
                customerService.updateCardBalance(customerId, replenishment);
            });

            verify(customerRepository, times(1)).findById(customerId);
            verifyNoMoreInteractions(customerRepository);
        }

        @Test
        @DisplayName("Should throw an exception when the replenishment is negative and exceeds the current balance")
        void update_card_balance_when_replenishment_is_negative_and_exceeds_current_balance() {
            Long customerId = 1L;
            Double replenishment = -100.0;

            Customer customer = Customer.builder()
                    .id(customerId)
                    .role(Account.Role.CUSTOMER)
                    .state(Account.State.CONFIRMED)
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("John")
                    .surname("Doe")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.of(1990, 1, 1))
                    .address(Address.builder()
                            .location(Location.builder().country("Russia").city("Moscow").build())
                            .street("Spartakovskaya")
                            .house(98)
                            .apartment("6A")
                            .build())
                    .cardBalance(50.0)
                    .build();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

            assertThrows(PaymentImpossibleException.class, () -> {
                customerService.updateCardBalance(customerId, replenishment);
            });

            verify(customerRepository, times(1)).findById(customerId);
            verifyNoMoreInteractions(customerRepository);
        }

        @Test
        @DisplayName("Should update the card balance when the replenishment is positive and does not exceed the current balance")
        void update_card_balance_success() {
            Long customerId = 1L;
            Double replenishment = 100.0;

            Customer customer = Customer.builder()
                    .id(customerId)
                    .role(Account.Role.CUSTOMER)
                    .state(Account.State.CONFIRMED)
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("John")
                    .surname("Doe")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.of(1990, 1, 1))
                    .address(Address.builder()
                            .location(Location.builder().country("Russia").city("Moscow").build())
                            .street("Spartakovskaya")
                            .house(98)
                            .apartment("6A")
                            .build())
                    .cardBalance(500.0)
                    .build();

            Customer updatedCustomer = Customer.builder()
                    .id(customerId)
                    .role(Account.Role.CUSTOMER)
                    .state(Account.State.CONFIRMED)
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("John")
                    .surname("Doe")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.of(1990, 1, 1))
                    .address(Address.builder()
                            .location(Location.builder().country("Russia").city("Moscow").build())
                            .street("Spartakovskaya")
                            .house(98)
                            .apartment("6A")
                            .build())
                    .cardBalance(500.0 + replenishment)
                    .build();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
            when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);

            CustomerDto result = customerService.updateCardBalance(customerId, replenishment);

            assertNotNull(result);
            assertEquals(600.0, result.getCardBalance());

            verify(customerRepository, times(1)).findById(customerId);
            verify(customerRepository, times(1)).save(customer);
            verifyNoMoreInteractions(customerRepository);
        }


    }




}