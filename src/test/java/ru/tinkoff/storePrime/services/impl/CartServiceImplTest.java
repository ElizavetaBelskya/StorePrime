package ru.tinkoff.storePrime.services.impl;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import ru.tinkoff.storePrime.dto.cart.CartItemDto;
import ru.tinkoff.storePrime.exceptions.not_found.CartItemNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.CustomerNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.ProductNotFoundException;
import ru.tinkoff.storePrime.models.CartItem;
import ru.tinkoff.storePrime.models.Product;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.CartRepository;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.repository.ProductRepository;
import ru.tinkoff.storePrime.repository.SellerRepository;
import ru.tinkoff.storePrime.services.utils.AccountCachingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        AccountCachingUtil accountCachingUtil = new AccountCachingUtil(cacheManager, customerRepository, sellerRepository);
        cartService.setAccountCachingUtil(accountCachingUtil);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("addNewCartItem() is working")
    public class AddNewCartItem {

        @Test
        @DisplayName("Should throw a CustomerNotFoundException when the customer does not exist")
        void add_new_CartItem_when_customer_does_not_exist() {
            Long customerId = 1L;
            Long productId = 2L;
            Integer quantity = 1;

            when(cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId)).thenReturn(Optional.empty());
            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
            when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(Product.builder().id(1L).build()));

            assertThrows(CustomerNotFoundException.class, () -> {
                cartService.addNewCartItem(customerId, productId, quantity);
            });

            verify(customerRepository, times(1)).findById(customerId);
            verifyNoMoreInteractions(customerRepository);

        }

        @Test
        @DisplayName("Should throw a ProductNotFoundException when the product does not exist")
        void add_new_CartItem_when_product_does_not_exist() {
            Long customerId = 1L;
            Long productId = 2L;
            Integer quantity = 1;

            when(cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId)).thenReturn(Optional.empty());
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () -> {
                cartService.addNewCartItem(customerId, productId, quantity);
            });

            verify(productRepository, times(1)).findById(productId);
            verify(customerRepository, never()).findById(anyLong());
            verify(cartRepository, never()).save(any(CartItem.class));
        }

        @Test
        @DisplayName("Should update the quantity of the cart item if it already exists")
        void add_new_CartItem_when_CartItem_already_exists_update_quantity() {
            Long customerId = 1L;
            Long productId = 2L;
            Integer quantity = 3;

            Customer customer = Customer.builder()
                    .id(customerId)
                    .build();

            Product product = Product.builder()
                    .id(productId)
                    .categories(new ArrayList<>())
                    .seller(Seller.builder().id(1L).build())
                    .build();

            CartItem existingCartItem = CartItem.builder()
                    .customer(customer)
                    .product(product)
                    .quantity(2)
                    .build();

            when(cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId)).thenReturn(Optional.of(existingCartItem));
            when(cartRepository.save(any(CartItem.class))).thenReturn(existingCartItem);

            CartItemDto result = cartService.addNewCartItem(customerId, productId, quantity);

            assertEquals(quantity, result.getQuantity());
            verify(cartRepository, times(1)).save(existingCartItem);
        }

        @Test
        @DisplayName("Should add a new cart item when the product and customer exist")
        void add_new_CartItem_when_product_and_customer_exist() {
            Long customerId = 1L;
            Long productId = 2L;
            Integer quantity = 3;

            Customer customer = Customer.builder()
                    .id(customerId)
                    .build();

            Product product = Product.builder()
                    .id(productId)
                    .seller(Seller.builder().id(1L).build())
                    .categories(new ArrayList<>())
                    .build();

            CartItem existingCartItem = CartItem.builder()
                    .customer(customer)
                    .product(product)
                    .quantity(2)
                    .build();

            when(cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId)).thenReturn(Optional.of(existingCartItem));
            when(cartRepository.save(any(CartItem.class))).thenReturn(existingCartItem);

            CartItemDto result = cartService.addNewCartItem(customerId, productId, quantity);

            assertNotNull(result);
            assertEquals(existingCartItem.getId(), result.getId());
            assertEquals(existingCartItem.getQuantity(), result.getQuantity());
            assertEquals(existingCartItem.getCustomer().getId(), result.getCustomerId());
            assertEquals(existingCartItem.getProduct().getId(), result.getProduct().getId());

            verify(cartRepository, times(1)).findByCustomer_IdAndProduct_Id(customerId, productId);
            verify(cartRepository, times(1)).save(any(CartItem.class));
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getCustomerCart() is working")
    public class GetCustomerCartTest {

        @Test
        @DisplayName("Should throw an exception when the customer id is not found")
        void get_customer_cart_when_customerId_is_not_found() {
            Long customerId = 1L;

            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class, () -> {
                cartService.getCustomerCart(customerId);
            });

            verify(customerRepository, times(1)).findById(customerId);
            verifyNoMoreInteractions(customerRepository);
        }


        @Test
        @DisplayName("Should return the customer's cart when the customer id is valid")
        void get_customer_cart_success() {
            Long customerId = 1L;
            Long productId = 1L;
            Long sellerId = 1L;
            Customer customer = Customer.builder().id(customerId).build();
            List<CartItem> cartItems = new ArrayList<>();
            cartItems.add(CartItem.builder()
                    .id(1L)
                    .customer(customer)
                    .product(Product.builder().id(productId).categories(new ArrayList<>()).seller(Seller.builder().id(sellerId).build()).build()).quantity(3)
                    .build());
            customer.setCart(cartItems);
            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
            Assert.assertEquals(CartItemDto.from(cartItems), cartService.getCustomerCart(customerId));
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("deleteProductFromCart() is working")
    public class DeleteProductFromCartTest {

        @Test
        @DisplayName("Should throw an exception when the customer ID and product ID combination does not exist in the cart")
        void delete_product_from_cart_when_customer_and_product_id_combination_does_not_exist() {
            Long customerId = 1L;
            Long productId = 2L;

            when(cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId)).thenReturn(Optional.empty());

            assertThrows(CartItemNotFoundException.class, () -> {
                cartService.deleteProductFromCart(customerId, productId);
            });

            verify(cartRepository, times(1)).findByCustomer_IdAndProduct_Id(customerId, productId);
            verifyNoMoreInteractions(cartRepository);
        }

        @Test
        @DisplayName("Should delete the product from the cart when the customer and product IDs are valid")
        void delete_product_from_cart_success() {
            Long customerId = 1L;
            Long productId = 2L;
            Long sellerId = 1L;

            CartItem foundCartItem = CartItem.builder().id(1L)
                    .product(Product.builder().id(productId)
                            .seller(Seller.builder().id(sellerId).build())
                            .categories(new ArrayList<>()).build()).build();
            when(cartRepository.findByCustomer_IdAndProduct_Id(customerId, productId)).thenReturn(Optional.of(foundCartItem));
            doNothing().when(cartRepository).delete(foundCartItem);
            assertDoesNotThrow(() -> cartService.deleteProductFromCart(customerId, productId));
        }

    }


}