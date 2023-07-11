package ru.tinkoff.storePrime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.storePrime.controller.api.ProductApi;
import ru.tinkoff.storePrime.dto.product.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.dto.product.ProductsPage;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.services.ProductService;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductDto> getProductById(Long id) {
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @Override
    public ResponseEntity<ProductDto> getRandomProduct() {
        return ResponseEntity.ok(productService.getOneProduct());
    }

    @Override
    public ResponseEntity<List<ProductDto>> getRandomProducts(Integer amount) {
        return ResponseEntity.ok(productService.getRandomProducts(amount));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductsByContentString(String content, String category) {
        if (category == null) {
            return ResponseEntity.ok().body(productService.getAllProductsByContentString(content));
        } else {
            return ResponseEntity.ok().body(productService.getAllProductsByContentStringAndCategory(content, category));
        }

    }

    @Override
    public ResponseEntity<ProductsPage> getProducts(int page, Double minPrice, Double maxPrice, String category, Long sellerId) {
        return ResponseEntity.ok().body(productService.getProductsPage(page, minPrice, maxPrice, category, sellerId));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts(Double minPrice, Double maxPrice, String category, Long sellerId) {
        return ResponseEntity.ok().body(productService.getAllProducts(minPrice, maxPrice, category, sellerId));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductsBySellerId(Long sellerId) {
        return ResponseEntity.ok().body(productService.getProductsBySellerId(sellerId));
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
        return ResponseEntity.ok().body(productService.updateProduct(id, sellerId, updatedProduct));
    }

    @Override
    public ResponseEntity<Void> deleteProductById(UserDetailsImpl userDetailsImpl, Long productId) {
        Long sellerId = userDetailsImpl.getAccount().getId();
        productService.deleteProduct(sellerId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
