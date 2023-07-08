package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.storePrime.dto.order.OrderDto;
import ru.tinkoff.storePrime.exceptions.DisparateDataException;
import ru.tinkoff.storePrime.exceptions.ForbiddenException;
import ru.tinkoff.storePrime.exceptions.not_found.CartItemNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.CustomerNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.OrderNotFoundException;
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
import java.util.List;

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
    public List<OrderDto> createNewOrders(Long customerId, List<Long> cartItemIdList) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("Пользователь с id " + customerId + " не найден"));
        List<CartItem> items = new ArrayList<>();
        for (Long itemId: cartItemIdList) {
            CartItem newItem = cartRepository.findById(itemId).orElseThrow(
                    () -> new CartItemNotFoundException("Товар в корзине с id " + customerId + " не найден"));
            if (!newItem.getCustomer().getId().equals(customerId)) {
                throw new ForbiddenException("Этот пользователь не имеет прав на обращение к элементу корзины с id " + itemId);
            } else {
                items.add(newItem);
            }
        }

        List<Order> orders = new ArrayList<>();
        for (CartItem item: items) {
            Product product = item.getProduct();
            if (product.getAmount() < item.getQuantity()) {
                throw new DisparateDataException("Запрос товара в заказе превышает его реальное колчество");
            }
            Double price = product.getPrice()*item.getQuantity();
            sellerService.updateCardBalanceBySellerId(product.getSeller().getId(), price*0.97);
            customerService.updateCardBalance(item.getCustomer().getId(), -1*price);
            product.setAmount(product.getAmount() - item.getQuantity());
            Order order = Order.builder()
                    .status(Order.Status.CREATED)
                    .product(product)
                    .quantity(item.getQuantity())
                    .customer(customer)
                    .build();
            Order newOrder = orderRepository.save(order);
            orders.add(newOrder);
        }

        return OrderDto.from(orders);
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
        Order.Status newStatus;
        try {
            newStatus = Order.Status.valueOf(status);
        } catch (IllegalArgumentException ex) {
            throw new DisparateDataException("Данный статус не относится к возможным статусам заказа");
        }
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Заказ с id "+ orderId +" не найден"));
        Product product = order.getProduct();
        if (product.getSeller().getId().equals(sellerId)) {
            order.setStatus(newStatus);
            return OrderDto.from(orderRepository.save(order));
        }
        throw new ForbiddenException("Этот продавец не имеет права на редактирование заказа с id " + orderId);
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long customerId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Этот заказ не найден"));
        if (order.getCustomer().getId().equals(customerId)) {
            order.setStatus(Order.Status.CANCELLED);
            Double orderPrice = order.getProduct().getPrice();
            customerService.updateCardBalance(customerId, orderPrice);
            sellerService.updateCardBalanceBySellerId(order.getProduct().getSeller().getId(), -orderPrice*0.97);
            return OrderDto.from(orderRepository.save(order));
        }
        throw new ForbiddenException("Этот покупатель не имеет права на редактирование заказа с id " + orderId);
    }

    @Override
    public List<OrderDto> getCancelledOrdersByCustomerId(Long customerId) {
        return OrderDto.from(orderRepository.findByCustomer_IdAndStatus(customerId, Order.Status.CANCELLED));
    }

}
