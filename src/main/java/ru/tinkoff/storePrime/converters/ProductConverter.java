package ru.tinkoff.storePrime.converters;

import ru.tinkoff.storePrime.dto.product.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.models.Product;

public class ProductConverter {

    public static Product getProductFromNewOrUpdateProductDto(NewOrUpdateProductDto productDto) {
        return Product.builder()
                .title(productDto.getTitle())
                .description(productDto.getDescription())
                .amount(productDto.getAmount())
                .price(productDto.getPrice())
                .build();
    }

}
