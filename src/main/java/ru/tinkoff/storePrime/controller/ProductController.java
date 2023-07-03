package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.ProductApi;
import ru.tinkoff.storePrime.dto.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.ProductDto;
import ru.tinkoff.storePrime.dto.ProductsPage;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.services.ProductService;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductDto> getProductById(Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductsBySellerId(Long sellerId) {
        return ResponseEntity.ok(productService.getProductsBySellerId(sellerId));
    }

    @Override
    public ResponseEntity<ProductDto> addProduct(UserDetailsImpl userDetailsImpl, NewOrUpdateProductDto newProduct) {
        Long sellerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(sellerId, newProduct));
    }

    @Override
    public ResponseEntity<ProductDto> updateProductById(UserDetailsImpl userDetailsImpl,
                                                        Long id, NewOrUpdateProductDto updatedProduct) {
        Long sellerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(productService.updateProduct(id, sellerId, updatedProduct));
    }

    @Override
    public ResponseEntity<ProductDto> deleteProductById(UserDetailsImpl userDetailsImpl, Long productId) {
        Long sellerId = userDetailsImpl.getAccount().getId();
        return ResponseEntity.ok(productService.deleteProduct(sellerId, productId));
    }

    @Override
    public ResponseEntity<ProductsPage> getProducts(int page, Double price, String category, Long sellerId) {
        return ResponseEntity.ok(productService.getProductsPage(page, price, category, sellerId));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts(Double price, String category, Long sellerId) {
        return ResponseEntity.ok(productService.getAllProducts(price, category, sellerId));
    }

}
