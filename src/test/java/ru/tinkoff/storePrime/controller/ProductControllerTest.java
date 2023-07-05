package ru.tinkoff.storePrime.controller;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.storePrime.controller.handler.RestExceptionHandler;
import ru.tinkoff.storePrime.dto.product.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.dto.product.ProductsPage;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.services.ProductService;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void tearDown() {
        Mockito.validateMockitoUsage();
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getProductById() is working")
    class GetProductByIdTest {


        @BeforeEach
        public void setUp() {
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

            ProductDto expectedProductDto3 = ProductDto.builder()
                    .id(14L)
                    .title("Product 3")
                    .description("Test Product Description 3")
                    .price(14.40)
                    .sellerId(1L)
                    .categories(Arrays.asList("Test category"))
                    .amount(499)
                    .build();

            when(productService.getProductById(12L)).thenReturn(expectedProductDto);
            when(productService.getProductById(23321L)).thenThrow(NoSuchElementException.class);
            when(productService.getProductById(-12L)).thenThrow(NoSuchElementException.class);
        }

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

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getProductByContent() is working")
    class GetProductByContentStringTest {

        @BeforeEach
        public void setUp() {
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

            ProductDto expectedProductDto3 = ProductDto.builder()
                    .id(14L)
                    .title("Product 3")
                    .description("Test Product Description 3")
                    .price(14.40)
                    .sellerId(1L)
                    .categories(Arrays.asList("Test category"))
                    .amount(499)
                    .build();

            when(productService.getAllProductsByContentString("Test")).thenReturn(Arrays.asList(expectedProductDto, expectedProductDto2));
            when(productService.getAllProductsByContentString("nonexistent")).thenReturn(new ArrayList<>());

        }

        @Test
        void get_products_by_content_string_when_no_products_match() throws Exception {
            mockMvc.perform(get("/products/search?content=nonexistent"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        void get_products_by_content_string() throws Exception {
            String content = "Test";
            mockMvc.perform(get("/products/search?content=" + content))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("Test Product"))
                    .andExpect(jsonPath("$[1].title").value("Test Product 2"));
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getProducts() is working")
    class GetProductsTest {
        @Test
        @DisplayName("Should return products page when valid parameters are provided")
        void get_products_when_valid_parameters_are_provided() throws Exception {
            int page = 1;
            Double minPrice = 10.0;
            Double maxPrice = 100.0;
            String category = "Electronics";
            Long sellerId = 1L;
            List<ProductDto> expectedProducts = Arrays.asList(
                    ProductDto.builder()
                            .id(1L)
                            .title("Product 1")
                            .description("Description 1")
                            .price(50.0)
                            .sellerId(1L)
                            .categories(Arrays.asList("Electronics"))
                            .amount(10)
                            .build(),
                    ProductDto.builder()
                            .id(2L)
                            .title("Product 2")
                            .description("Description 2")
                            .price(80.0)
                            .sellerId(1L)
                            .categories(Arrays.asList("Electronics"))
                            .amount(5)
                            .build()
            );

            ProductsPage expectedPage = ProductsPage.builder()
                    .products(expectedProducts)
                    .totalPagesCount(2)
                    .build();

            when(productService.getProductsPage(page, minPrice, maxPrice, category, sellerId))
                    .thenReturn(expectedPage);

            mockMvc.perform(get("/products/pages")
                            .param("page", String.valueOf(page))
                            .param("minPrice", String.valueOf(minPrice))
                            .param("maxPrice", String.valueOf(maxPrice))
                            .param("category", category)
                            .param("sellerId", String.valueOf(sellerId))
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products[0].title").value("Product 1"));

        }

        @Test
        @DisplayName("Should return products page filtered by maximum price when maxPrice is provided")
        void get_products_when_maxPrice_is_provided() throws Exception {
            int page = 1;
            Double minPrice = null;
            Double maxPrice = 100.0;
            String category = null;
            Long sellerId = null;

            List<ProductDto> expectedProducts = Arrays.asList(
                    ProductDto.builder()
                            .id(12L)
                            .title("Test Product")
                            .description("Test Product Description")
                            .price(19.99)
                            .sellerId(1L)
                            .categories(Arrays.asList("Pet", "Test category"))
                            .amount(10)
                            .build(),
                    ProductDto.builder()
                            .id(13L)
                            .title("Test Product 2")
                            .description("Test Product Description 2")
                            .price(483.0)
                            .sellerId(1L)
                            .categories(Arrays.asList("Test category"))
                            .amount(6)
                            .build()
            );

            when(productService.getProductsPage(page, minPrice, maxPrice, category, sellerId))
                    .thenReturn(new ProductsPage(expectedProducts, 1));

            mockMvc.perform(get("/products/pages?page=1&maxPrice=100.0"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.products").isArray())
                    .andExpect(jsonPath("$.products.length()").value(2))
                    .andExpect(jsonPath("$.products[0].title").value("Test Product"))
                    .andExpect(jsonPath("$.products[1].title").value("Test Product 2"))
                    .andExpect(jsonPath("$.totalPagesCount").value(1));
        }

        @Test
        @DisplayName("Should return products page filtered by category when category is provided")
        void get_products_when_category_is_provided() throws Exception {
            int page = 1;
            Double minPrice = 10.0;
            Double maxPrice = 100.0;
            String category = "Electronics";
            Long sellerId = null;

            List<ProductDto> expectedProducts = Arrays.asList(
                    ProductDto.builder()
                            .id(1L)
                            .title("Product 1")
                            .description("Description 1")
                            .price(50.0)
                            .sellerId(1L)
                            .categories(Arrays.asList("Electronics"))
                            .amount(10)
                            .build(),
                    ProductDto.builder()
                            .id(2L)
                            .title("Product 2")
                            .description("Description 2")
                            .price(80.0)
                            .sellerId(2L)
                            .categories(Arrays.asList("Electronics"))
                            .amount(5)
                            .build()
            );

            ProductsPage expectedPage = ProductsPage.builder()
                    .products(expectedProducts)
                    .totalPagesCount(1)
                    .build();

            when(productService.getProductsPage(page, minPrice, maxPrice, category, sellerId))
                    .thenReturn(expectedPage);

            mockMvc.perform(get("/products/pages")
                            .param("page", String.valueOf(page))
                            .param("minPrice", String.valueOf(minPrice))
                            .param("maxPrice", String.valueOf(maxPrice))
                            .param("category", category))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products").isArray())
                    .andExpect(jsonPath("$.products.length()").value(expectedProducts.size()))
                    .andExpect(jsonPath("$.products[0].title").value("Product 1"))
                    .andExpect(jsonPath("$.products[1].title").value("Product 2"))
                    .andExpect(jsonPath("$.totalPagesCount").value(1));
        }

        @Test
        @DisplayName("Should throw an exception when invalid category is provided")
        void get_products_when_invalid_category_is_provided_then_throw_exception() throws Exception {
            int page = 1;
            Double minPrice = 10.0;
            Double maxPrice = 100.0;
            String category = "InvalidCategory";
            Long sellerId = null;

            when(productService.getProductsPage(page, minPrice, maxPrice, category, sellerId))
                    .thenReturn(ProductsPage.builder().products(new ArrayList<>()).totalPagesCount(0).build());

            mockMvc.perform(get("/products/pages")
                            .param("page", String.valueOf(page))
                            .param("minPrice", String.valueOf(minPrice))
                            .param("maxPrice", String.valueOf(maxPrice))
                            .param("category", category)
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products").isEmpty());

        }

        @Test
        @DisplayName("Should throw an exception when invalid price range is provided")
        void get_products_when_invalid_price_range_is_provided_then_throw_exception() throws Exception {
            int page = 1;
            Double minPrice = 100.0;
            Double maxPrice = 50.0;
            String category = "Electronics";
            Long sellerId = null;
            when(productService.getProductsPage(page, minPrice, maxPrice, category, sellerId))
                    .thenThrow(IllegalArgumentException.class);
            mockMvc.perform(get("/products/pages")
                            .param("page", String.valueOf(page))
                            .param("minPrice", String.valueOf(minPrice))
                            .param("maxPrice", String.valueOf(maxPrice))
                            .param("category", category))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should throw an exception when invalid page number is provided")
        void get_products_when_invalid_page_number_isProvided_then_throw_exception() throws Exception {
            int page = -1;
            Double minPrice = 10.0;
            Double maxPrice = 100.0;
            String category = "Electronics";
            Long sellerId = null;

            when(productService.getProductsPage(page, minPrice, maxPrice, category, sellerId))
                    .thenThrow(IllegalArgumentException.class);

            mockMvc.perform(get("/products/pages")
                            .param("page", String.valueOf(page))
                            .param("minPrice", String.valueOf(minPrice))
                            .param("maxPrice", String.valueOf(maxPrice))
                            .param("category", category)).andDo(print())
                    .andExpect(status().isBadRequest());

        }

    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("addProducts() is working")
    class AddProductTest {

        private UserDetails userDetails;

        @BeforeEach
        public void setUpUserDetails() {
            userDetails = new UserDetailsImpl(Seller.builder().id(1L).email("myemail@mail.ru").passwordHash("1234qwer").role(Account.Role.SELLER).state(Account.State.CONFIRMED).build());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singleton(new SimpleGrantedAuthority("SELLER"))));
        }

        @Test
        public void test_add_correct_product() throws Exception {
            NewOrUpdateProductDto newProduct = new NewOrUpdateProductDto();
            newProduct.setTitle("Книга");
            newProduct.setDescription("Отличная книга для чтения");
            newProduct.setPrice(19.99);
            newProduct.setCategories(Arrays.asList("Категория 1", "Категория 2"));
            newProduct.setAmount(10);

            ProductDto addedProduct = new ProductDto();
            addedProduct.setId(1L);
            addedProduct.setTitle("Книга");
            addedProduct.setDescription("Отличная книга для чтения");
            addedProduct.setPrice(19.99);
            addedProduct.setCategories(Arrays.asList("Категория 1", "Категория 2"));
            addedProduct.setAmount(10);
            when(productService.addProduct(1L, newProduct)).thenReturn(addedProduct);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(newProduct);

            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("Книга"))
                    .andExpect(jsonPath("$.description").value("Отличная книга для чтения"));
        }

        @Test
        public void test_add_incorrect_product() throws Exception {
            NewOrUpdateProductDto newProduct = new NewOrUpdateProductDto();
            newProduct.setTitle("Книга");
            newProduct.setDescription("Отличная книга для чтения");
            newProduct.setPrice(-1.9);
            newProduct.setCategories(Arrays.asList("Категория 1", "Категория 2"));
            newProduct.setAmount(-1);

            ProductDto addedProduct = new ProductDto();
            addedProduct.setId(1L);
            addedProduct.setTitle("Книга");
            addedProduct.setDescription("Отличная книга для чтения");
            addedProduct.setPrice(19.99);
            addedProduct.setCategories(Arrays.asList("Категория 1", "Категория 2"));
            addedProduct.setAmount(10);
            when(productService.addProduct(1L, newProduct)).thenReturn(addedProduct);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(newProduct);

            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[0].fieldName").value("amount"))
                    .andExpect(jsonPath("$.errors[0].objectName").value("newOrUpdateProductDto"))
                    .andExpect(jsonPath("$.errors[1].fieldName").value("price"))
                    .andExpect(jsonPath("$.errors[1].objectName").value("newOrUpdateProductDto"));
        }

    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("updateProducts() is working")
    public class UpdateProductTest {

        private ObjectMapper objectMapper = new ObjectMapper();

        private UserDetails userDetails;

        @BeforeEach
        public void setUpUserDetails() {
            userDetails = new UserDetailsImpl(Seller.builder().id(1L).email("myemail@mail.ru")
                    .passwordHash("1234qwer").role(Account.Role.SELLER).state(Account.State.CONFIRMED).build());
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singleton(
                            new SimpleGrantedAuthority("SELLER"))));
        }

        @Test
        public void test_update_correct_product() throws Exception {
            Long productId = 1L;
            Long sellerId = 1L;
            NewOrUpdateProductDto updatedProduct =  NewOrUpdateProductDto.builder()
                    .title("Книга")
                    .description("Отличная книга для чтения")
                    .price(19.99)
                    .categories(Arrays.asList("Категория 1", "Категория 2"))
                    .amount(10)
                    .build();

            ProductDto updatedProductDto = ProductDto.builder()
                    .id(productId)
                    .title("Книга")
                    .description("Отличная книга для чтения")
                    .price(19.99)
                    .categories(Arrays.asList("Категория 1", "Категория 2"))
                    .amount(10)
                    .sellerId(1L)
                    .build();
            when(productService.updateProduct(productId, sellerId, updatedProduct)).thenReturn(updatedProductDto);

            mockMvc.perform(put("/products/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedProduct)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.id").value(updatedProductDto.getId()))
                    .andExpect(jsonPath("$.title").value(updatedProductDto.getTitle()));
        }

        @Test
        public void test_update_incorrect_product() throws Exception {
            Long productId = 1L;
            Long sellerId = 1L;
            NewOrUpdateProductDto updatedProduct =  NewOrUpdateProductDto.builder()
                    .title("Книга")
                    .description(null)
                    .price(-1.4)
                    .categories(Arrays.asList("Категория 1", "Категория 2"))
                    .amount(10)
                    .build();

            ProductDto updatedProductDto = ProductDto.builder()
                    .id(productId)
                    .title("Книга")
                    .description("Отличная книга для чтения")
                    .price(19.99)
                    .categories(Arrays.asList("Категория 1", "Категория 2"))
                    .amount(10)
                    .sellerId(1L)
                    .build();
            when(productService.updateProduct(productId, sellerId, updatedProduct)).thenReturn(updatedProductDto);

            mockMvc.perform(put("/products/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedProduct)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[0].fieldName").value("price"))
                    .andDo(print());
        }

        @Test
        public void test_update_nonexistent_product() throws Exception {
            Long productId = 1L;
            Long sellerId = 1L;
            NewOrUpdateProductDto updatedProduct =  NewOrUpdateProductDto.builder()
                    .title("Книга")
                    .description("Отличная книга для чтения")
                    .price(19.99)
                    .categories(Arrays.asList("Категория 1", "Категория 2"))
                    .amount(10)
                    .build();

            when(productService.updateProduct(productId, sellerId, updatedProduct))
                    .thenThrow(new NoSuchElementException("Товар не найден"));

            mockMvc.perform(put("/products/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedProduct)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Товар не найден"))
                    .andDo(print())
                    .andReturn();
        }

        @Test
        public void test_update_product_of_another_seller() throws Exception {
            Long productId = 1L;
            Long sellerId = 9L;
            NewOrUpdateProductDto updatedProduct =  NewOrUpdateProductDto.builder()
                    .title("Книга")
                    .description("Отличная книга для чтения")
                    .price(19.99)
                    .categories(Arrays.asList("Категория 1", "Категория 2"))
                    .amount(10)
                    .build();

            //TODO: доделать нормально
            when(productService.updateProduct(productId, sellerId, updatedProduct))
                    .thenThrow(new IllegalArgumentException("Товар не найден"));

            mockMvc.perform(put("/products/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedProduct)))
                    .andDo(print())
                    .andReturn();
        }



    }



}
