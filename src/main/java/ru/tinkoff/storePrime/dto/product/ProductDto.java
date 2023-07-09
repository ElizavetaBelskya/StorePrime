package ru.tinkoff.storePrime.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.models.Product;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Товар")
public class ProductDto {

    @Schema(description = "Идентификатор товара", example = "123")
    private Long id;

    @Schema(description = "Название товара", example = "Книга")
    private String title;

    @Schema(description = "Описание товара", example = "Отличная книга для чтения")
    private String description;

    @Schema(description = "Цена товара", example = "19.99")
    private Double price;

    @Schema(description = "Идентификатор продавца товара")
    private Long sellerId;

    @Schema(description = "Категории товара")
    private List<String> categories;

    @Schema(description = "Количество товара")
    private Integer amount;


}
