package ru.tinkoff.storePrime.controller;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.storePrime.aspects.RestExceptionHandler;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.services.ProductService;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("ProductController is working when")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ProductDto expectedProductDto = ProductDto.builder()
                .id(12L)
                .title("Test Product")
                .description("Test Product Description")
                .price(19.99)
                .sellerId(1L)
                .categories(Arrays.asList("Pet", "Test category"))
                .amount(10)
                .build();

        ProductDto expectedProductDto2 = ProductDto.builder()
                .id(13L)
                .title("Test Product 2")
                .description("Test Product Description 2")
                .price(483.0)
                .sellerId(1L)
                .categories(Arrays.asList("Test category"))
                .amount(6)
                .build();

        when(productService.getProductById(12L)).thenReturn(expectedProductDto);
        when(productService.getProductById(23321L)).thenThrow(NoSuchElementException.class);
        when(productService.getProductById(-12L)).thenThrow(NoSuchElementException.class);
        when(productService.getProductsBySellerId(1L)).thenReturn(Arrays.asList(expectedProductDto, expectedProductDto2));

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getProductById() is working")
    class GetProductByIdTest {

        @Test
        public void return_product_by_id() throws Exception {
            mockMvc.perform(get("/products/12"))
                    .andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Test Product"));
        }

        @Test
        public void return_not_found_for_non_existent_product() throws Exception {
            mockMvc.perform(get("/products/23321"))
                    .andDo(print()).andExpect(status().isNotFound());
        }

        @Test
        public void return_bad_request_for_invalid_id_format() throws Exception {
            mockMvc.perform(get("/products/text")).andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }




}
