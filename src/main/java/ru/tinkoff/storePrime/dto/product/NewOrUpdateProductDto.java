package ru.tinkoff.storePrime.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.models.Category;
import ru.tinkoff.storePrime.models.user.Seller;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Новый товар")
public class NewOrUpdateProductDto {

    @Schema(description = "Название товара", example = "Книга")
    @NotBlank(message = "Название товара не может быть пустым")
    private String title;

    @Schema(description = "Описание товара", example = "Отличная книга для чтения")
    private String description;

    @Schema(description = "Цена товара", example = "19.99")
    @NotNull(message = "Цена товара не может быть пустой")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена товара должна быть положительной")
    private Double price;

    @Schema(description = "Категории товара")
    @NotEmpty(message = "Список категорий не может быть пустым")
    private List<String> categories;

    @Schema(description = "Количество товара")
    @NotNull(message = "Количество товара не может быть пустым")
    @Min(value = 0, message = "Количество товара должно быть неотрицательным")
    private Integer amount;

}
