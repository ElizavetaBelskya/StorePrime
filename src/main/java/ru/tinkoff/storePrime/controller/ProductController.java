package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.ProductApi;
import ru.tinkoff.storePrime.dto.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.ProductDto;
import ru.tinkoff.storePrime.services.ProductService;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductDto> addProduct(NewOrUpdateProductDto newProduct) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductsBySellerId(Long sellerId) {
        return ResponseEntity.ok(productService.getProductsBySellerId(sellerId));
    }

    @Override
    public ResponseEntity<ProductDto> updateProductById(Long id, NewOrUpdateProductDto updatedProduct) {
        return null;
    }

}
