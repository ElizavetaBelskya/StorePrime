package ru.tinkoff.storePrime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.tinkoff.storePrime.controller.handler.RestExceptionHandler;
import ru.tinkoff.storePrime.dto.location.AddressDto;
import ru.tinkoff.storePrime.dto.location.LocationDto;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.models.Address;
import ru.tinkoff.storePrime.models.Location;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.services.CustomerService;

import java.time.LocalDate;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerController is working when")
class CustomerControllerTest {

    private static MockMvc mockMvc;
    @Mock
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController(customerService))
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(@NotNull MethodParameter parameter) {
                        return parameter.getParameterType().isAssignableFrom(UserDetailsImpl.class);
                    }

                    @Override
                    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return new UserDetailsImpl(
                                Customer.builder()
                                        .id(1L).email("example@mail.ru")
                                        .cardBalance(500.0)
                                        .gender(Customer.Gender.MALE)
                                        .birthdayDate(LocalDate.parse("2000-01-01"))
                                        .address(Address.builder()
                                                .location(Location.builder()
                                                        .country("Россия")
                                                        .city("Москва")
                                                        .build())
                                                .build())
                                        .build()
                        );
                    }
                }).build();
    }

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
                    .phoneNumber("89999999999")
                    .name("Иван")
                    .surname("Иванов")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2000-01-01"))
                    .addressDto(AddressDto.builder().location(
                            LocationDto.builder()
                                    .country("Россия").city("Москва")
                                    .build()).build())
                    .passwordHash("passwordHash123!")
                    .build();


            CustomerDto savedCustomer = CustomerDto.builder()
                    .email("example@mail.ru")
                    .id(1L)
                    .phoneNumber("89999999999")
                    .name("Иван")
                    .surname("Иванов")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate("2000-01-01")
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
                    .andDo(print());

        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("updateCustomer() is working")
    public class UpdateCustomerTest {

        @Test
        void test_update_customer_success() throws Exception {
            NewOrUpdateCustomerDto newCustomer = NewOrUpdateCustomerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("89090999998")
                    .name("Иван")
                    .surname("Ивановский")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate(LocalDate.parse("2000-01-01"))
                    .addressDto(AddressDto.builder().location(
                            LocationDto.builder()
                                    .country("Россия").city("Москва")
                                    .build()).build())
                    .passwordHash("passwordHash123!")
                    .build();
            CustomerDto savedCustomer = CustomerDto.builder()
                    .id(1L)
                    .email("example@mail.ru")
                    .phoneNumber("89090999998")
                    .name("Иван")
                    .surname("Ивановский")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate("2000-01-01")
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
        void test_update_incorrect_customer() throws Exception {
            NewOrUpdateCustomerDto newCustomer = NewOrUpdateCustomerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("89090999333333339")
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
                    .andExpect(status().isBadRequest())
                    .andDo(print());

        }


    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("deleteCustomer() is working")
    public class DeleteCustomerTest {

        @Test
        void test_delete_customer_success() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/customer"))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("updateCustomerCardBalance() is working")
    public class UpdateCustomerCardBalanceTest {
        @Test
        void test_update_customer_card_balance_success() throws Exception {
            CustomerDto savedCustomer = CustomerDto.builder()
                    .id(1L)
                    .name("Иван")
                    .surname("Иванов")
                    .email("example@mail.ru")
                    .cardBalance(500.0)
                    .phoneNumber("89093249999")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate("2000-01-01")
                    .addressDto(AddressDto.builder()
                            .location(LocationDto.builder()
                                    .country("Россия")
                                    .city("Москва")
                                    .build())
                            .build())
                    .build();
            savedCustomer.setCardBalance(savedCustomer.getCardBalance() + 30.0);
            when(customerService.updateCardBalance(eq(1L), eq(30.0))).thenReturn(savedCustomer);
            String jsonPayload = "30";
            mockMvc.perform(MockMvcRequestBuilders.patch("/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.surname").value(savedCustomer.getSurname()))
                    .andDo(print());
        }

        @Test
        void test_update_customer_card_balance_bad_request() throws Exception {
            CustomerDto savedCustomer = CustomerDto.builder()
                    .id(1L)
                    .name("Иван")
                    .surname("Иванов")
                    .email("example@mail.ru")
                    .cardBalance(500.0)
                    .phoneNumber("89093249999")
                    .gender(Customer.Gender.MALE)
                    .birthdayDate("2000-01-01")
                    .addressDto(AddressDto.builder()
                            .location(LocationDto.builder()
                                    .country("Россия")
                                    .city("Москва")
                                    .build())
                            .build())
                    .build();
            savedCustomer.setCardBalance(savedCustomer.getCardBalance() + 30.0);
            String jsonPayload = "{ывапрол}";
            mockMvc.perform(MockMvcRequestBuilders.patch("/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

    }

}