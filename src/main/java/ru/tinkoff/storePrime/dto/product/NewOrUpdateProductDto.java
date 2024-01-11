package ru.tinkoff.storePrime.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Новый товар")
public class NewOrUpdateProductDto {

    @Schema(description = "Название товара", example = "Книга")
    @NotBlank(message = "{product.title.notBlank}")
    @Size(min = 1, max = 100, message = "{product.title.size}")
    private String title;

    @Schema(description = "Описание товара", example = "Отличная книга для чтения")
    private String description;

    @Schema(description = "Цена товара", example = "19.99")
    @NotNull(message = "{product.price.notNull}")
    @DecimalMin(value = "1.0", inclusive = false, message = "{product.price.min}")
    private Double price;

    @Schema(description = "Категории товара")
    private List<String> categories;

    @Schema(description = "Количество товара")
    @NotNull(message = "{product.amount.notNull}")
    @Min(value = 1, message = "{product.amount.min}")
    private Integer amount;

    @Schema(description = "Идентификаторы иллюстраций")
    @NotEmpty(message = "{product.imageIds.notEmpty}")
    @Size(min = 1, max = 3)
    private List<String> imageIds;

}
