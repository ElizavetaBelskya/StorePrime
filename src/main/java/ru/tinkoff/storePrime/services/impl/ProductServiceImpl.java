package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.converters.ProductConverter;
import ru.tinkoff.storePrime.dto.product.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.dto.product.ProductsPage;
import ru.tinkoff.storePrime.exceptions.DisparateDataException;
import ru.tinkoff.storePrime.exceptions.ForbiddenException;
import ru.tinkoff.storePrime.exceptions.not_found.CategoryNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.ProductNotFoundException;
import ru.tinkoff.storePrime.exceptions.not_found.SellerNotFoundException;
import ru.tinkoff.storePrime.models.Category;
import ru.tinkoff.storePrime.models.Product;
import ru.tinkoff.storePrime.repository.CategoryRepository;
import ru.tinkoff.storePrime.repository.ProductRepository;
import ru.tinkoff.storePrime.repository.SellerRepository;
import ru.tinkoff.storePrime.services.ProductService;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${default.page-size}")
    private int defaultPageSize;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final SellerRepository sellerRepository;

    @Override
    public ProductDto getProductById(Long id) {
        return ProductConverter.getProductDtoFromProduct(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Товар с id " + id + " не найден")));
    }

    @Override
    public List<ProductDto> getProductsBySellerId(Long sellerId) {
        return ProductConverter.getProductDtoFromProduct(productRepository.findAllBySellerId(sellerId));
    }

    @Override
    public ProductDto addProduct(Long sellerId, NewOrUpdateProductDto newProductDto) {
        Product newProduct = ProductConverter.getProductFromNewOrUpdateProductDto(newProductDto);
        newProduct.setSeller(sellerRepository.findById(sellerId).orElseThrow(() -> new SellerNotFoundException("")));
        List<Category> categories = newProductDto.getCategories().stream()
                .flatMap(name -> categoryRepository.findByName(name).stream())
                .collect(Collectors.toList());
        newProduct.setCategories(categories);
        return ProductConverter.getProductDtoFromProduct(productRepository.save(newProduct));
    }

    @Override
    public ProductDto updateProduct(Long productId, Long sellerId, NewOrUpdateProductDto updatedProductDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Товар с id " + productId + " не найден"));
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new ForbiddenException("Товар с id " + productId + " не доступен для редактирования данным продавцом");
        }
        Product updatedProduct = ProductConverter.getProductFromNewOrUpdateProductDto(updatedProductDto);
        updatedProduct.setId(productId);
        updatedProduct.setSeller(product.getSeller());
        List<Category> categories = updatedProductDto.getCategories().stream()
                .flatMap(name -> categoryRepository.findByName(name).stream())
                .collect(Collectors.toList());
        updatedProduct.setCategories(categories);
        return ProductConverter.getProductDtoFromProduct(productRepository.save(updatedProduct));
    }

    @Override
    public void deleteProduct(Long sellerId, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Товар с id " + productId + " не найден"));
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new ForbiddenException("Товар с id " + productId + " не доступен для удаления данным продавцом");
        }
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getAllProducts(Double minPrice, Double maxPrice, String category, Long sellerId) {
        if (minPrice > maxPrice) {
            throw new DisparateDataException("Минимальная цена больше максимальной");
        }
        Optional<Category> categoryToSearch = categoryRepository.findByName(category);
        Collection<Category> categories = Collections.emptyList();
        if (categoryToSearch.isPresent()) {
            categories = Collections.singleton(categoryToSearch.get());
        } else {
            throw new DisparateDataException("Эта категория не существует");
        }
        if (minPrice == null) {
            minPrice = 0.0;
        }
        if (sellerId != null && maxPrice != null) {
            if (!categories.isEmpty()) {
                return ProductConverter.getProductDtoFromProduct(productRepository.findBySellerAndPriceAndCategory(sellerId, minPrice, maxPrice, categories));
            } else {
                return ProductConverter.getProductDtoFromProduct(productRepository.findBySellerAndPrice(sellerId, minPrice, maxPrice));
            }
        } else if (maxPrice != null) {
            if (!categories.isEmpty()) {
                return ProductConverter.getProductDtoFromProduct(productRepository.findByPriceAndCategory(minPrice, maxPrice, categories));
            } else {
                return ProductConverter.getProductDtoFromProduct(productRepository.findByPrice(minPrice, maxPrice));
            }
        } else {
            if (!categories.isEmpty()) {
                return ProductConverter.getProductDtoFromProduct(productRepository.findByCategory(categories));
            } else {
                return ProductConverter.getProductDtoFromProduct(productRepository.findAll());
            }
        }
    }

    @Override
    public ProductsPage getProductsPage(int page, Double minPrice, Double maxPrice, String category, Long sellerId) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Optional<Category> categoryToSearch = categoryRepository.findByName(category);
        Collection<Category> categories = Collections.emptyList();
        if (categoryToSearch.isPresent()) {
            categories = Collections.singleton(categoryToSearch.get());
        }
        if (maxPrice == null && minPrice != null) {
            maxPrice = Double.MAX_VALUE;
            //TODO: тут бы заменить на чтото разумное
        }
        if (minPrice == null) {
            minPrice = 0d;
        }
        Page<Product> productsPage;
        if (sellerId != null && maxPrice != null) {
            if (!categories.isEmpty()) {
                productsPage = productRepository.findPageBySellerAndPriceAndCategory(pageRequest, sellerId, minPrice, maxPrice, categories);
            } else {
                productsPage = productRepository.findPageBySellerAndPrice(pageRequest, sellerId, minPrice, maxPrice);
            }
        } else if (maxPrice != null) {
            if (!categories.isEmpty()) {
                productsPage = productRepository.findPageByPriceAndCategory(pageRequest, minPrice, maxPrice, categories);
            } else {
                productsPage = productRepository.findPageByPrice(pageRequest, minPrice, maxPrice);
            }
        } else {
            if (!categories.isEmpty()) {
                productsPage = productRepository.findPageByCategory(pageRequest, categories);
            } else {
                productsPage = productRepository.findPage(pageRequest);
            }
        }

        return ProductsPage.builder()
                .products(ProductConverter.getProductDtoFromProduct(productsPage.getContent()))
                .totalPagesCount(productsPage.getTotalPages())
                .build();

    }

    @Override
    public List<ProductDto> getAllProductsByContentString(String content) {
        return ProductConverter.getProductDtoFromProduct(productRepository.findAllByContent("%" + content + "%"));
    }

}
