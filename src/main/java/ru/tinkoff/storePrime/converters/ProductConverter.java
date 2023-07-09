package ru.tinkoff.storePrime.converters;

import ru.tinkoff.storePrime.dto.product.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductConverter {

    private ProductConverter(){};

    public static Product getProductFromNewOrUpdateProductDto(NewOrUpdateProductDto productDto) {
        return Product.builder()
                .title(productDto.getTitle())
                .description(productDto.getDescription())
                .amount(productDto.getAmount())
                .price(productDto.getPrice())
                .build();
    }

    public static ProductDto getProductDtoFromProduct(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .sellerId(product.getSeller().getId())
                .amount(product.getAmount())
                .categories(
                        product.getCategories().stream()
                                .map(x -> x.getName())
                                .collect(Collectors.toList())
                ).build();
    }

    public static List<ProductDto> getProductDtoFromProduct(List<Product> products) {
        return products.stream().map(ProductConverter::getProductDtoFromProduct)
                .collect(Collectors.toList());
    }

}
