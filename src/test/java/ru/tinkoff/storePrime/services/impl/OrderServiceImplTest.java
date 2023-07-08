
package ru.tinkoff.storePrime.services.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.storePrime.dto.order.OrderDto;
import ru.tinkoff.storePrime.exceptions.DisparateDataException;
import ru.tinkoff.storePrime.exceptions.ForbiddenException;
import ru.tinkoff.storePrime.exceptions.not_found.CartItemNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.CustomerNotFoundException;
import ru.tinkoff.storePrime.models.CartItem;
import ru.tinkoff.storePrime.models.Product;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.CartRepository;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.repository.OrderRepository;
import ru.tinkoff.storePrime.services.CustomerService;
import ru.tinkoff.storePrime.services.SellerService;
import ru.tinkoff.storePrime.models.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private SellerService sellerService;

    @Mock
    private CustomerService customerService;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("createNewOrders() is working")
    public class CreateNewOrdersTest {

        @Test
        @DisplayName("Should throw an exception when customer is not found")
        void create_new_orders_when_customer_not_found_then_throw_exception() {
            Long customerId = 1L;
            List<Long> cartItemIdList = List.of(1L, 2L, 3L);
            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
            assertThrows(CustomerNotFoundException.class, () -> orderService.createNewOrders(customerId, cartItemIdList));
            verify(customerRepository, times(1)).findById(customerId);
            verifyNoMoreInteractions(customerRepository, cartRepository, orderRepository, sellerService, customerService);
        }

        @Test
        @DisplayName("Should throw an exception when customer does not have rights to access cart item")
        void create_new_orders_when_customer_has_no_rights_to_cart_item_then_throw_exception() {
            Long customerId = 1L;
            List<Long> cartItemIdList = Arrays.asList(1L, 2L, 3L);

            Customer customer = Customer.builder()
                    .id(customerId)
                    .build();

            Customer anotherCustomer = Customer.builder()
                    .id(4L)
                    .build();

            CartItem cartItem1 = CartItem.builder()
                    .id(1L)
                    .product(Product.builder().id(1L).build())
                    .quantity(3)
                    .customer(customer)
                    .build();

            CartItem cartItem2 = CartItem.builder()
                    .id(2L)
                    .product(Product.builder().id(2L).build())
                    .quantity(2)
                    .customer(customer)
                    .build();

            CartItem cartItem3 = CartItem.builder()
                    .id(3L)
                    .product(Product.builder().id(3L).build())
                    .quantity(3)
                    .customer(anotherCustomer)
                    .build();

            List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2, cartItem3);

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
            when(cartRepository.findById(1L)).thenReturn(Optional.of(cartItem1));
            when(cartRepository.findById(2L)).thenReturn(Optional.of(cartItem2));
            when(cartRepository.findById(3L)).thenReturn(Optional.of(cartItem3));

            assertThrows(ForbiddenException.class, () -> {
                orderService.createNewOrders(customerId, cartItemIdList);
            });

            verify(customerRepository, times(1)).findById(customerId);
            verify(cartRepository, times(1)).findById(1L);
            verify(cartRepository, times(1)).findById(2L);
            verify(cartRepository, times(1)).findById(3L);
        }

        @Test
        @DisplayName("Should throw an exception when cart item is not found")
        void create_new_orders_when_CartItem_not_found() {
            Long customerId = 1L;
            List<Long> cartItemIdList = Arrays.asList(1L, 2L, 3L);
            Customer customer = Customer.builder()
                    .id(customerId)
                    .build();

            CartItem cartItem1 = CartItem.builder()
                    .id(1L)
                    .customer(customer)
                    .build();

            CartItem cartItem2 = CartItem.builder()
                    .id(2L)
                    .customer(customer)
                    .build();

            CartItem cartItem3 = CartItem.builder()
                    .id(3L)
                    .customer(customer)
                    .build();
            List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2, cartItem3);

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
            when(cartRepository.findById(1L)).thenReturn(Optional.of(cartItem1));
            when(cartRepository.findById(2L)).thenReturn(Optional.of(cartItem2));
            when(cartRepository.findById(3L)).thenReturn(Optional.empty());

            OrderServiceImpl orderService = new OrderServiceImpl(cartRepository, orderRepository, customerRepository, sellerService, customerService);
            assertThrows(CartItemNotFoundException.class, () -> orderService.createNewOrders(customerId, cartItemIdList));
        }

        @Test
        @DisplayName("Should create new orders when customer and cart items are valid")
        void create_new_orders_success() {
            Long customerId = 1L;
            List<Long> cartItemIdList = Arrays.asList(1L, 2L, 3L);

            Customer customer = Customer.builder().id(customerId).build();

            Product product1 = Product.builder()
                    .id(1L)
                    .amount(10)
                    .price(100.0)
                    .seller(Seller.builder().id(1L).build())
                    .categories(new ArrayList<>())
                    .build();

            Product product2 = Product.builder()
                    .id(2L)
                    .amount(5)
                    .price(120.0)
                    .seller(Seller.builder().id(2L).build())
                    .categories(new ArrayList<>())
                    .build();

            Product product3 = Product.builder()
                    .id(3L)
                    .amount(3)
                    .price(30.0)
                    .seller(Seller.builder().id(3L).build())
                    .categories(new ArrayList<>())
                    .build();

            CartItem cartItem1 = CartItem.builder()
                    .id(1L)
                    .customer(customer)
                    .quantity(2)
                    .product(product1)
                    .build();

            CartItem cartItem2 = CartItem.builder()
                    .id(2L)
                    .customer(customer)
                    .quantity(1)
                    .product(product2)
                    .build();

            CartItem cartItem3 = CartItem.builder()
                    .id(3L)
                    .customer(customer)
                    .quantity(2)
                    .product(product3)
                    .build();

            List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2, cartItem3);

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
            when(cartRepository.findById(1L)).thenReturn(Optional.of(cartItem1));
            when(cartRepository.findById(2L)).thenReturn(Optional.of(cartItem2));
            when(cartRepository.findById(3L)).thenReturn(Optional.of(cartItem3));

            doAnswer(invocation -> null).when(sellerService).updateCardBalanceBySellerId(eq(1L), anyDouble());
            doAnswer(invocation -> null).when(sellerService).updateCardBalanceBySellerId(eq(2L), anyDouble());
            doAnswer(invocation -> null).when(sellerService).updateCardBalanceBySellerId(eq(3L), anyDouble());
            doAnswer(invocation -> null).when(customerService).updateCardBalance(eq(customerId), anyDouble());

            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
                Order order = invocation.getArgument(0);
                order.setId(1L);
                return order;
            });

            List<OrderDto> result = orderService.createNewOrders(customerId, cartItemIdList);

            assertEquals(3, result.size());
            assertEquals(1L, result.get(0).getId());
            assertEquals(1L, result.get(0).getCustomerId());
            assertEquals("CREATED", result.get(0).getStatus());
            assertEquals(1L, result.get(0).getProduct().getId());
        }

        @Test
        @DisplayName("Should throw an exception when requested product quantity exceeds available quantity")
        void create_new_orders_when_requested_product_quantity_exceeds_available() {
            Long customerId = 1L;
            List<Long> cartItemIdList = Arrays.asList(1L, 2L, 3L);
            Customer customer = Customer.builder().id(customerId).build();
            Product product1 = Product.builder()
                    .id(1L)
                    .amount(10)
                    .price(100.0)
                    .seller(Seller.builder().id(1L).build())
                    .categories(new ArrayList<>())
                    .build();

            Product product2 = Product.builder()
                    .id(2L)
                    .amount(5)
                    .price(120.0)
                    .seller(Seller.builder().id(2L).build())
                    .categories(new ArrayList<>())
                    .build();

            Product product3 = Product.builder()
                    .id(3L)
                    .amount(0)
                    .price(30.0)
                    .seller(Seller.builder().id(3L).build())
                    .categories(new ArrayList<>())
                    .build();

            CartItem cartItem1 = CartItem.builder()
                    .id(1L)
                    .customer(customer)
                    .quantity(2)
                    .product(product1)
                    .build();

            CartItem cartItem2 = CartItem.builder()
                    .id(2L)
                    .customer(customer)
                    .quantity(1)
                    .product(product2)
                    .build();

            CartItem cartItem3 = CartItem.builder()
                    .id(3L)
                    .customer(customer)
                    .quantity(2)
                    .product(product3)
                    .build();

            List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2, cartItem3);

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
            when(cartRepository.findById(1L)).thenReturn(Optional.of(cartItem1));
            when(cartRepository.findById(2L)).thenReturn(Optional.of(cartItem2));
            when(cartRepository.findById(3L)).thenReturn(Optional.of(cartItem3));

            assertThrows(DisparateDataException.class, () -> orderService.createNewOrders(customerId, cartItemIdList));
        }

    }






}
