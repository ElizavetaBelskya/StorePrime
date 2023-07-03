package ru.tinkoff.storePrime.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Страница с товарами и общее количество страниц")
public class ProductsPage {

    @Schema(description = "Список товаров")
    private List<ProductDto> products;

    @Schema(description = "Общее количество страниц", example = "5")
    private Integer totalPagesCount;

}
