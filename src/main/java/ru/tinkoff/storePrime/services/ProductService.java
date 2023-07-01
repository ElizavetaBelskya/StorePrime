package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto getProductById(Long id);

    List<ProductDto> getProductsBySellerId(Long sellerId);
}
