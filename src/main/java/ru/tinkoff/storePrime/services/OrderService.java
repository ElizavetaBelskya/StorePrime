package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.order.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> createNewOrders(Long customerId, List<Long> cartItemIdList);

    List<OrderDto> getAllOrdersOfCustomer(Long customerId);

    List<OrderDto> getAllOrdersOfSeller(Long sellerId);

    OrderDto changeStatus(Long sellerId, Long orderId, String status);

    OrderDto cancelOrder(Long customerId, Long orderId);

    List<OrderDto> getCancelledOrdersByCustomerId(Long customerId);
}
