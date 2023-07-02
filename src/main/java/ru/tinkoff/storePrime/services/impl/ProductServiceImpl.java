package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.converters.ProductConverter;
import ru.tinkoff.storePrime.dto.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.ProductDto;
import ru.tinkoff.storePrime.models.Category;
import ru.tinkoff.storePrime.models.Product;
import ru.tinkoff.storePrime.models.user.Seller;
import ru.tinkoff.storePrime.repository.CategoryRepository;
import ru.tinkoff.storePrime.repository.ProductRepository;
import ru.tinkoff.storePrime.repository.SellerRepository;
import ru.tinkoff.storePrime.services.ProductService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final SellerRepository sellerRepository;

    @Override
    public ProductDto getProductById(Long id) {
        return ProductDto.from(productRepository.findById(id).orElseThrow());
    }

    @Override
    public List<ProductDto> getProductsBySellerId(Long sellerId) {
        return ProductDto.from(productRepository.findAllBySellerId(sellerId));
    }

    @Override
    public ProductDto addProduct(Long sellerId, NewOrUpdateProductDto newProductDto) {
        Product newProduct = ProductConverter.getProductFromNewOrUpdateProductDto(newProductDto);
        newProduct.setSeller(sellerRepository.findById(sellerId).get());
        List<Category> categories = newProductDto.getCategories().stream()
                .flatMap(name -> categoryRepository.findByName(name).stream())
                .collect(Collectors.toList());
        newProduct.setCategories(categories);
        return ProductDto.from(productRepository.save(newProduct));
    }

    @Override
    public ProductDto updateProduct(Long id, Long sellerId, NewOrUpdateProductDto updatedProductDto) {
        Product product = productRepository.findById(id).orElseThrow();
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new IllegalArgumentException();
            //TODO: подумать что тут можно выбросить
        }
        Product updatedProduct = ProductConverter.getProductFromNewOrUpdateProductDto(updatedProductDto);
        updatedProduct.setId(id);
        updatedProduct.setSeller(product.getSeller());
        List<Category> categories = updatedProductDto.getCategories().stream()
                .flatMap(name -> categoryRepository.findByName(name).stream())
                .collect(Collectors.toList());
        updatedProduct.setCategories(categories);
        return ProductDto.from(productRepository.save(updatedProduct));
    }

    @Override
    public ProductDto deleteProduct(Long sellerId, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new IllegalArgumentException();
            //TODO: подумать что тут можно выбросить
        }
        productRepository.delete(product);
        return new ProductDto();
    }

}
