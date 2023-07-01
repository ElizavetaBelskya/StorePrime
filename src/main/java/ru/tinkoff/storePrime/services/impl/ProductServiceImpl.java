package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.dto.ProductDto;
import ru.tinkoff.storePrime.repository.ProductRepository;
import ru.tinkoff.storePrime.services.ProductService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductDto getProductById(Long id) {
        return ProductDto.from(productRepository.findById(id).orElseThrow());
    }

    @Override
    public List<ProductDto> getProductsBySellerId(Long sellerId) {
        return ProductDto.from(productRepository.findAllBySellerId(sellerId));
    }

}
