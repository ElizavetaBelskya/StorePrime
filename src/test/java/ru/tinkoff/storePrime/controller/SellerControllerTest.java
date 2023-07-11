package ru.tinkoff.storePrime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.tinkoff.storePrime.controller.handler.RestExceptionHandler;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.user.SellerDto;
import ru.tinkoff.storePrime.exceptions.not_found.SellerNotFoundException;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.services.AccountService;
import ru.tinkoff.storePrime.services.SellerService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@DisplayName("SellerController is working when")
public class SellerControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private SellerService sellerService;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SellerController(accountService, sellerService))
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().isAssignableFrom(UserDetailsImpl.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
                        return new UserDetailsImpl(
                                Seller.builder()
                                        .id(1L).email("example@mail.ru")
                                        .passwordHash("password")
                                        .state(Account.State.CONFIRMED)
                                        .role(Account.Role.SELLER)
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
    @DisplayName("addSeller() is working")
    public class AddSellerTest {

        @Test
        @DisplayName("Should throw an exception when the email is already used")
        public void add_seller_when_email_is_already_used() throws Exception {
            NewOrUpdateSellerDto newSeller = NewOrUpdateSellerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("89999999999")
                    .name("Ivan")
                    .passwordHash("Password123!!")
                    .INN("1234567890")
                    .description("Our company is a market leader")
                    .build();

            when(accountService.isEmailUsed(newSeller.getEmail())).thenReturn(true);
            mockMvc.perform(post("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newSeller)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isConflict())
                    .andReturn();
        }

        @Test
        @DisplayName("Should create a new seller")
        public void add_seller_success() throws Exception {
            NewOrUpdateSellerDto newSeller = NewOrUpdateSellerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("89999999999")
                    .name("Ivan")
                    .passwordHash("Password123!!")
                    .INN("1234567890")
                    .description("Our company is a market leader")
                    .build();
            SellerDto savedSeller = SellerDto.builder()
                    .id(1L)
                    .email("example@mail.ru")
                    .phoneNumber("89999999999")
                    .name("Ivan")
                    .description("Our company is a market leader")
                    .build();

            when(accountService.isEmailUsed(newSeller.getEmail())).thenReturn(false);
            when(sellerService.addSeller(newSeller)).thenReturn(savedSeller);
            mockMvc.perform(post("/seller")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(newSeller)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();
        }

        @Test
        @DisplayName("Should throw an exception: incorrect seller")
        public void add_incorrect_seller() throws Exception {
            NewOrUpdateSellerDto newSeller = NewOrUpdateSellerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("899999999234567890")
                    .name("45")
                    .description(null)
                    .build();
            when(accountService.isEmailUsed(newSeller.getEmail())).thenReturn(false);

            mockMvc.perform(post("/seller")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(newSeller)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andReturn();
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("updateSeller() is working")
    public class UpdateSellerTest {

        @Test
        @DisplayName("Should throw an exception when the email is already used")
        public void update_seller_when_email_is_already_used() throws Exception {
            NewOrUpdateSellerDto newSeller = NewOrUpdateSellerDto.builder()
                    .email("wrong@mail.ru")
                    .phoneNumber("89999999999")
                    .name("Ivan")
                    .description("Our company is a market leader")
                    .INN("1234567890")
                    .passwordHash("passwordHash123!")
                    .build();

            when(sellerService.updateSeller(1L, newSeller)).thenThrow(AlreadyExistsException.class);
            mockMvc.perform(put("/seller")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(newSeller)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isConflict())
                    .andReturn();
        }

        @Test
        @DisplayName("Should update a new seller")
        public void update_seller_success() throws Exception {
            NewOrUpdateSellerDto newSeller = NewOrUpdateSellerDto.builder()
                    .email("wrong@mail.ru")
                    .phoneNumber("89999999999")
                    .name("Ivan")
                    .description("Our company is a market leader")
                    .INN("1234567890")
                    .passwordHash("passwordHash123!")
                    .build();
            SellerDto savedSeller = SellerDto.builder()
                    .id(1L)
                    .email("wrong@mail.ru")
                    .phoneNumber("89999999999")
                    .name("Ivan")
                    .description("Our company is a market leader")
                    .build();

            when(sellerService.updateSeller(1L, newSeller)).thenReturn(savedSeller);
            mockMvc.perform(post("/seller")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(newSeller)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getSellerById() is working")
    public class GetSellerByIdTest {

        @Test
        @DisplayName("Should a new seller")
        public void get_seller_success() throws Exception {
            SellerDto seller = SellerDto.builder()
                    .id(1L)
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("Ivan")
                    .description("Our company is a market leader")
                    .build();

            when(sellerService.getSeller(1L)).thenReturn(seller);
            mockMvc.perform(get("/seller/1"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andReturn();
        }

        @Test
        @DisplayName("Should throw an exception: seller not found")
        public void get_seller_not_found() throws Exception {
            when(sellerService.getSeller(55L))
                    .thenThrow(new SellerNotFoundException("Такой продавец не существует"));

            mockMvc.perform(get("/seller/55"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getThisSeller() is working")
    public class GetThisSellerTest {
        @Test
        @DisplayName("Should a new seller")
        public void get_seller_success() throws Exception {
            SellerDto seller = SellerDto.builder()
                    .id(1L)
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("Ivan")
                    .description("Our company is a market leader")
                    .build();

            when(sellerService.getSeller(1L)).thenReturn(seller);
            mockMvc.perform(get("/seller/1"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andReturn();
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("deleteSeller() is working")
    public class DeleteSellerTest {
        @Test
        @DisplayName("Should delete a new seller")
        public void delete_seller_success() throws Exception {
            mockMvc.perform(delete("/seller"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();
        }

    }


}
