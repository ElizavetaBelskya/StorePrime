package ru.tinkoff.storePrime.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.storePrime.dto.location.AddressDto;
import ru.tinkoff.storePrime.dto.location.LocationDto;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.models.Address;
import ru.tinkoff.storePrime.models.Location;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.services.CustomerService;
import ru.tinkoff.storePrime.services.ProductService;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerController is working when")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService customerService;

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("addCustomer() is working")
    public class AddCustomerTest {

        @Test
        void test_add_correct_customer() throws Exception {
            NewOrUpdateCustomerDto newCustomer = NewOrUpdateCustomerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("Иван")
                    .surname("Иванов")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2000-01-01"))
                    .addressDto(AddressDto.builder().location(
                            LocationDto.builder()
                                    .country("Россия").city("Москва")
                                    .build()).build())
                    .passwordHash("passwordHash")
                    .build();


            CustomerDto savedCustomer = CustomerDto.builder().email("example@mail.ru")
                    .id(1L)
                    .phoneNumber("899999999")
                    .name("Иван")
                    .surname("Иванов")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2000-01-01"))
                    .addressDto(AddressDto.builder().location(
                            LocationDto.builder()
                                    .country("Россия").city("Москва")
                                    .build()).build())
                    .build();

            when(customerService.addCustomer(newCustomer)).thenReturn(savedCustomer);

            mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(newCustomer)))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(savedCustomer.getName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(savedCustomer.getEmail()))
                    .andDo(print());

        }

        @Test
        void test_add_incorrect_customer() throws Exception {
            NewOrUpdateCustomerDto newCustomer = NewOrUpdateCustomerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("89999")
                    .name("Иван")
                    .surname("Иванов")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2026-01-01"))
                    .addressDto(AddressDto.builder().location(
                            LocationDto.builder()
                                    .country("Россия").city("Москва")
                                    .build()).build())
                    .passwordHash("passwordHash")
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(newCustomer)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[0].fieldName").value("birthdayDate"))
                    .andDo(print());

        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("addCustomer() is working")
    public class UpdateCustomerTest {

        private UserDetails userDetails;

        @Test
        void test_update_customer_success() throws Exception {
            userDetails = new UserDetailsImpl(Customer.builder()
                    .id(1L)
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("Иван")
                    .surname("Иванов")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2000-01-01"))
                    .address(Address.builder().location(new Location("Россия", "Москва")).build())
                    .role(Account.Role.CUSTOMER)
                    .state(Account.State.CONFIRMED).build());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singleton(new SimpleGrantedAuthority("CUSTOMER"))));

            NewOrUpdateCustomerDto newCustomer = NewOrUpdateCustomerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("890909999")
                    .name("Иван")
                    .surname("Ивановский")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2000-01-01"))
                    .addressDto(AddressDto.builder().location(
                            LocationDto.builder()
                                    .country("Россия").city("Москва")
                                    .build()).build())
                    .passwordHash("passwordHash")
                    .build();
            CustomerDto savedCustomer = CustomerDto.builder().email("example@mail.ru")
                    .id(1L)
                    .phoneNumber("890909999")
                    .name("Иван")
                    .surname("Ивановский")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2000-01-01"))
                    .addressDto(AddressDto.builder().location(
                            LocationDto.builder()
                                    .country("Россия").city("Москва")
                                    .build()).build())
                    .build();
            when(customerService.updateCustomer(1L, newCustomer)).thenReturn(savedCustomer);

            mockMvc.perform(MockMvcRequestBuilders.put("/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(newCustomer)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.surname").value(savedCustomer.getSurname()))
                    .andDo(print());

        }

        @Test
        void test_update_customer_by_anon() throws Exception {
            NewOrUpdateCustomerDto newCustomer = NewOrUpdateCustomerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("890909999")
                    .name("Иван")
                    .surname("Ивановский")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2000-01-01"))
                    .addressDto(AddressDto.builder().location(
                            LocationDto.builder()
                                    .country("Россия").city("Москва")
                                    .build()).build())
                    .passwordHash("passwordHash")
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.put("/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(newCustomer)))
                    .andExpect(status().isUnauthorized())
                    .andDo(print());

        }

        @Test
        void test_update_customer_by_seller() throws Exception {
            userDetails = new UserDetailsImpl(Seller.builder()
                    .id(1L)
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("Иван")
                    .location(new Location("Россия", "Москва"))
                    .role(Account.Role.SELLER)
                    .state(Account.State.CONFIRMED).build());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singleton(new SimpleGrantedAuthority("SELLER"))));

            NewOrUpdateCustomerDto newCustomer = NewOrUpdateCustomerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("890909999")
                    .name("Иван")
                    .surname("Ивановский")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2000-01-01"))
                    .addressDto(AddressDto.builder().location(
                            LocationDto.builder()
                                    .country("Россия").city("Москва")
                                    .build()).build())
                    .passwordHash("passwordHash")
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.put("/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(newCustomer)))
                    .andExpect(status().isForbidden())
                    .andDo(print());

        }

    }


}