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
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.services.AccountService;
import ru.tinkoff.storePrime.services.SellerService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
    public class addSellerTest {

        @Test
        @DisplayName("Should throw an exception when the email is already used")
        public void add_seller_when_email_is_already_used() throws Exception {
            NewOrUpdateSellerDto newSeller = NewOrUpdateSellerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("Ivan")
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
        public void add_seller() throws Exception {
            NewOrUpdateSellerDto newSeller = NewOrUpdateSellerDto.builder()
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
                    .name("Ivan")
                    .description("Our company is a market leader")
                    .build();
            SellerDto savedSeller = SellerDto.builder()
                    .id(1L)
                    .email("example@mail.ru")
                    .phoneNumber("899999999")
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


    }




}
