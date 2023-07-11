package ru.tinkoff.storePrime.services.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.storePrime.dto.product.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.exceptions.ForbiddenException;
import ru.tinkoff.storePrime.exceptions.not_found.ProductNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.SellerNotFoundException;
import ru.tinkoff.storePrime.models.Category;
import ru.tinkoff.storePrime.models.Product;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.CategoryRepository;
import ru.tinkoff.storePrime.repository.ProductRepository;
import ru.tinkoff.storePrime.repository.SellerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService is working when")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private ProductServiceImpl productService;


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getProductById() is working")
    public class GetProductByIdTest {
        @Test
        @DisplayName("Should throw ProductNotFoundException when the product id does not exist")
        void get_product_by_id_when_product_id_does_not_exist() {
            Long productId = 123L;
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () -> {
                productService.getProductById(productId);
            });

            verify(productRepository, times(1)).findById(productId);
        }

        @Test
        @DisplayName("Should return the product when the product id exists")
        void get_product_by_id_success() {
            Long productId = 123L;
            Product product = new Product();
            product.setId(productId);
            product.setTitle("Test Product");
            product.setDescription("This is a test product");
            product.setPrice(19.99);
            product.setSeller(new Seller());
            product.setCategories(new ArrayList<>());
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            ProductDto result = productService.getProductById(productId);

            assertNotNull(result);
            assertEquals(productId, result.getId());
            assertEquals("Test Product", result.getTitle());
            assertEquals("This is a test product", result.getDescription());
            assertEquals(19.99, result.getPrice());
            assertEquals(product.getSeller().getId(), result.getSellerId());
            assertEquals(0, result.getCategories().size());

            verify(productRepository, times(1)).findById(productId);
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getProductById() is working")
    public class AddProductTest {
        @Test
        @DisplayName("Should throw a SellerNotFoundException when the seller does not exist")
        void add_product_when_seller_does_not_exist() {
            Long sellerId = 123L;
            NewOrUpdateProductDto newProductDto = new NewOrUpdateProductDto();
            newProductDto.setTitle("Test Product");
            newProductDto.setDescription("This is a test product");
            newProductDto.setPrice(19.99);
            newProductDto.setCategories(new ArrayList<>());

            when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

            assertThrows(SellerNotFoundException.class, () -> {
                productService.addProduct(sellerId, newProductDto);
            });

            verify(sellerRepository, times(1)).findById(sellerId);
        }

        @Test
        @DisplayName("Should return a ProductDto when a product is added successfully")
        void add_product_success() {
            Long sellerId = 1L;
            NewOrUpdateProductDto newProductDto = NewOrUpdateProductDto.builder()
                    .title("Test Product")
                    .description("This is a test product")
                    .price(19.99)
                    .categories(new ArrayList<>())
                    .amount(10)
                    .build();

            Seller seller = new Seller();
            seller.setId(sellerId);
            when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
                Product savedProduct = invocation.getArgument(0);
                savedProduct.setId(123L);
                return savedProduct;
            });

            ProductDto result = productService.addProduct(sellerId, newProductDto);

            assertNotNull(result);
            assertEquals(123L, result.getId());
            assertEquals("Test Product", result.getTitle());
            assertEquals("This is a test product", result.getDescription());
            assertEquals(19.99, result.getPrice());
            assertEquals(sellerId, result.getSellerId());
            assertEquals(0, result.getCategories().size());

            verify(sellerRepository, times(1)).findById(sellerId);
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Should set the categories of the product correctly when adding a product")
        void add_product_should_set_categories_correctly() {
            Long sellerId = 1L;
            NewOrUpdateProductDto newProductDto = new NewOrUpdateProductDto();
            newProductDto.setTitle("Test Product");
            newProductDto.setDescription("This is a test product");
            newProductDto.setPrice(19.99);
            newProductDto.setCategories(List.of("Category1", "Category2"));
            newProductDto.setAmount(10);

            Seller seller = new Seller();
            seller.setId(sellerId);
            when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));

            Category category1 = new Category();
            category1.setId(1L);
            category1.setName("Category1");
            Category category2 = new Category();
            category2.setId(2L);
            category2.setName("Category2");
            when(categoryRepository.findByName("Category1")).thenReturn(Optional.of(category1));
            when(categoryRepository.findByName("Category2")).thenReturn(Optional.of(category2));

            Product savedProduct = new Product();
            savedProduct.setId(123L);
            savedProduct.setTitle("Test Product");
            savedProduct.setDescription("This is a test product");
            savedProduct.setPrice(19.99);
            savedProduct.setSeller(seller);
            savedProduct.setCategories(List.of(category1, category2));
            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            ProductDto result = productService.addProduct(sellerId, newProductDto);

            assertNotNull(result);
            assertEquals(savedProduct.getId(), result.getId());
            assertEquals(savedProduct.getTitle(), result.getTitle());
            assertEquals(savedProduct.getDescription(), result.getDescription());
            assertEquals(savedProduct.getPrice(), result.getPrice());
            assertEquals(savedProduct.getSeller().getId(), result.getSellerId());
            assertEquals(savedProduct.getCategories().size(), result.getCategories().size());

            verify(sellerRepository, times(1)).findById(sellerId);
            verify(categoryRepository, times(2)).findByName(anyString());
            verify(productRepository, times(1)).save(any(Product.class));
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    public class UpdateProductTest {

        @Test
        @DisplayName("Should throw a ProductNotFoundException when the product id is invalid")
        void update_product_when_productId_is_invalid() {
            Long productId = 123L;
            Long sellerId = 1L;
            NewOrUpdateProductDto updatedProductDto = NewOrUpdateProductDto.builder()
                    .title("Updated Product")
                    .description("This is an updated product")
                    .price(29.99)
                    .categories(new ArrayList<>())
                    .amount(20)
                    .build();

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () -> {
                productService.updateProduct(productId, sellerId, updatedProductDto);
            });

            verify(productRepository, times(1)).findById(productId);
        }

        @Test
        @DisplayName("Should throw a ForbiddenException when the seller id is not the owner of the product")
        void update_product_when_sellerId_is_not_owner() {
            Long productId = 123L;
            Long sellerId = 456L;
            NewOrUpdateProductDto updatedProductDto = new NewOrUpdateProductDto();
            updatedProductDto.setTitle("Updated Product");
            updatedProductDto.setDescription("This is an updated product");
            updatedProductDto.setPrice(29.99);
            updatedProductDto.setCategories(new ArrayList<>());

            Product product = new Product();
            product.setId(productId);
            product.setTitle("Test Product");
            product.setDescription("This is a test product");
            product.setPrice(19.99);
            product.setSeller(Seller.builder().id(1L).email("example@mail.ru").name("Seller").role(Account.Role.SELLER).build());
            product.setCategories(new ArrayList<>());
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            assertThrows(ForbiddenException.class, () -> {
                productService.updateProduct(productId, sellerId, updatedProductDto);
            });

            verify(productRepository, times(1)).findById(productId);
        }

        @Test
        @DisplayName("Should update the product when the product id and seller id are valid and the seller owns the product")
        void update_product_successful() {
            Long productId = 123L;
            Long sellerId = 1L;
            NewOrUpdateProductDto updatedProductDto = NewOrUpdateProductDto.builder()
                    .title("Updated Product")
                    .description("This is an updated product")
                    .price(29.99)
                    .categories(new ArrayList<>())
                    .amount(20)
                    .build();
            Seller seller = Seller
                    .builder()
                    .id(sellerId)
                    .name("Tinkoff")
                    .email("email@mail.ru")
                    .cardBalance(66.0)
                    .role(Account.Role.SELLER)
                    .state(Account.State.CONFIRMED)
                    .build();
            Product existingProduct = new Product();
            existingProduct.setId(productId);
            existingProduct.setTitle("Test Product");
            existingProduct.setDescription("This is a test product");
            existingProduct.setPrice(19.99);
            existingProduct.setSeller(seller);
            existingProduct.setCategories(new ArrayList<>());

            when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ProductDto result = productService.updateProduct(productId, sellerId, updatedProductDto);

            assertNotNull(result);
            assertEquals(productId, result.getId());
            assertEquals("Updated Product", result.getTitle());
            assertEquals("This is an updated product", result.getDescription());
            assertEquals(29.99, result.getPrice());
            assertEquals(sellerId, result.getSellerId());
            assertEquals(0, result.getCategories().size());

            verify(productRepository, times(1)).findById(productId);
            verify(productRepository, times(1)).save(any(Product.class));
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    public class DeleteProductTest {

        @Test
        @DisplayName("Should throw an exception when the product does not exist")
        void delete_product_when_does_not_exist() {
            Long sellerId = 1L;
            Long productId = 123L;

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () -> {
                productService.deleteProduct(sellerId, productId);
            });

            verify(productRepository, times(1)).findById(productId);
        }

        @Test
        @DisplayName("Should throw an exception when the seller is not the owner of the product")
        void delete_product_seller_is_not_owner() {
            Long sellerId = 1L;
            Long productId = 123L;

            Seller seller = Seller.builder().id(2L).build();
            Product product = Product.builder()
                    .id(productId)
                    .seller(seller)
                    .build();

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            assertThrows(ForbiddenException.class, () -> {
                productService.deleteProduct(sellerId, productId);
            });

            verify(productRepository, times(1)).findById(productId);
            verify(productRepository, never()).delete(any(Product.class));
        }

        @Test
        @DisplayName("Should delete the product when the seller is the owner of the product")
        void delete_product_when_seller_is_owner() {
            Long sellerId = 1L;
            Long productId = 123L;

            Product product = new Product();
            product.setId(productId);
            product.setTitle("Test Product");
            product.setDescription("This is a test product");
            product.setPrice(19.99);
            product.setSeller(Seller.builder().id(1L).build());
            product.setCategories(new ArrayList<>());

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            productService.deleteProduct(sellerId, productId);

            verify(productRepository, times(1)).findById(productId);
            verify(productRepository, times(1)).delete(product);
        }

    }


}