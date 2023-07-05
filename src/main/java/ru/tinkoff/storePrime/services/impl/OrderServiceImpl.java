package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.storePrime.dto.order.OrderDto;
import ru.tinkoff.storePrime.models.CartItem;
import ru.tinkoff.storePrime.models.Order;
import ru.tinkoff.storePrime.models.Product;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.repository.CartRepository;
import ru.tinkoff.storePrime.repository.CustomerRepository;
import ru.tinkoff.storePrime.repository.OrderRepository;
import ru.tinkoff.storePrime.services.CustomerService;
import ru.tinkoff.storePrime.services.OrderService;
import ru.tinkoff.storePrime.services.SellerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final SellerService sellerService;

    private final CustomerService customerService;

    @Override
    @Transactional
    public OrderDto createNewOrder(Long customerId, List<Long> cartItemIdList) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        List<CartItem> items = new ArrayList<>();
        for (Long itemId: cartItemIdList) {
            CartItem newItem = cartRepository.findById(itemId).orElseThrow();
            if (!newItem.getCustomer().getId().equals(customerId)) {
                throw new IllegalArgumentException();
                //TODO: нормальное исключение
            } else {
                items.add(newItem);
            }
        }

        List<Product> productListForOrder = new ArrayList<>();
        Map<Product, Integer> productAmountsForOrder = new HashMap<>();
        for (CartItem item: items) {
            Product product = item.getProduct();
            sellerService.updateCardBalanceBySellerId(product.getSeller().getId(), product.getPrice());
            customerService.updateCardBalance(item.getCustomer().getId(), -1*product.getPrice());
            productListForOrder.add(product);
            productAmountsForOrder.put(product, item.getQuantity());
        }
        Order order = Order.builder()
                .status(Order.Status.CREATED)
                .products(productListForOrder)
                .productAmounts(productAmountsForOrder)
                .customer(customer)
                .build();
        orderRepository.save(order);
        return OrderDto.from(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getAllOrdersOfCustomer(Long customerId) {
        return OrderDto.from(orderRepository.getOrdersByCustomerId(customerId));
    }

    @Override
    public List<OrderDto> getAllOrdersOfSeller(Long sellerId) {
        return OrderDto.from(orderRepository.getOrdersByProductsSellerId(sellerId));
    }

    @Override
    public OrderDto changeStatus(Long sellerId, Long orderId, String status) {
        Order.Status newStatus = Order.Status.valueOf(status);
        //TODO: выбрасывает IllegalArgument
        Order order = orderRepository.findById(orderId).orElseThrow();
        for (Product product: order.getProducts()) {
            if (product.getSeller().getId().equals(sellerId)) {
                order.setStatus(newStatus);
                return OrderDto.from(orderRepository.save(order));
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public OrderDto cancelOrder(Long customerId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getCustomer().getId().equals(customerId)) {
            order.setStatus(Order.Status.CANCELLED);
            return OrderDto.from(orderRepository.save(order));
        }
        throw new IllegalArgumentException();
    }

}
