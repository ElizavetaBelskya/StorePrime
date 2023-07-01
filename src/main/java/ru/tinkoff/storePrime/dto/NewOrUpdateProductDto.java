package ru.tinkoff.storePrime.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.models.Category;
import ru.tinkoff.storePrime.models.user.Seller;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Новый товар")
public class NewOrUpdateProductDto {

    @Schema(description = "Название товара", example = "Книга")
    private String title;

    @Schema(description = "Описание товара", example = "Отличная книга для чтения")
    private String description;

    @Schema(description = "Цена товара", example = "19.99")
    private Double price;

    @Schema(description = "Категории товара")
    private List<String> categories;

    @Schema(description = "Количество товара")
    private Integer amount;

}
