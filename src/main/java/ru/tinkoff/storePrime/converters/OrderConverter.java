package ru.tinkoff.storePrime.converters;

import ru.tinkoff.storePrime.dto.order.OrderDto;
import ru.tinkoff.storePrime.models.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderConverter {

    private OrderConverter(){};

    public static OrderDto getOrderDtoFromOrder(Order order) {

        return OrderDto.builder().id(order.getId())
                .customerId(order.getCustomer().getId())
                .status(order.getStatus().name())
                .product(ProductConverter.getProductDtoFromProduct(order.getProduct()))
                .build();
    }

    public static List<OrderDto> getOrderDtoFromOrder(List<Order> orders) {
        return orders.stream().map(OrderConverter::getOrderDtoFromOrder)
                .collect(Collectors.toList());
    }

}
