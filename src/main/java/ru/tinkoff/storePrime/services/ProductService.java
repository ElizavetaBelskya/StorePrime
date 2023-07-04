package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.product.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.dto.product.ProductsPage;

import java.util.List;

public interface ProductService {
    ProductDto getProductById(Long id);

    List<ProductDto> getProductsBySellerId(Long sellerId);

    ProductDto addProduct(Long sellerId, NewOrUpdateProductDto newProductDto);

    ProductDto updateProduct(Long productId, Long sellerId, NewOrUpdateProductDto updatedProductDto);

    ProductDto deleteProduct(Long sellerId, Long productId);

    List<ProductDto> getAllProducts(Double minPrice, Double maxPrice, String category, Long sellerId);

    ProductsPage getProductsPage(int page, Double minPrice, Double maxPrice, String category, Long sellerId);

    List<ProductDto> getAllProductsByContentString(String content);
}
