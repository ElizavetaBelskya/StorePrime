package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.ProductDto;
import ru.tinkoff.storePrime.dto.ProductsPage;
import ru.tinkoff.storePrime.models.user.Seller;

import java.util.List;

public interface ProductService {
    ProductDto getProductById(Long id);

    List<ProductDto> getProductsBySellerId(Long sellerId);

    ProductDto addProduct(Long sellerId, NewOrUpdateProductDto newProductDto);

    ProductDto updateProduct(Long id, Long sellerId, NewOrUpdateProductDto updatedProductDto);

    ProductDto deleteProduct(Long sellerId, Long productId);

    List<ProductDto> getAllProducts(Double minPrice, Double maxPrice, String category, Long sellerId);

    ProductsPage getProductsPage(int page, Double minPrice, Double maxPrice, String category, Long sellerId);

    List<ProductDto> getAllProductsByContentString(String content);
}
