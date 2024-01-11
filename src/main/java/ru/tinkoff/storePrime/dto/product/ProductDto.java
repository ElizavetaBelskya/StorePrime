package ru.tinkoff.storePrime.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.base.LongIdDto;
import ru.tinkoff.storePrime.dto.location.LocationDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Товар")
public class ProductDto extends LongIdDto {

    @Schema(description = "Название товара", example = "Книга")
    private String title;

    @Schema(description = "Описание товара", example = "Отличная книга для чтения")
    private String description;

    @Schema(description = "Цена товара", example = "19.99")
    private Double price;

    @Schema(description = "Идентификатор продавца товара")
    private Long sellerId;

    @Schema(description = "Имя продавца товара")
    private String sellerName;

    private LocationDto sellerLocation;

    @Schema(description = "Категории товара")
    private List<String> categories;

    @Schema(description = "Количество товара")
    private Integer amount;

    @Schema(description = "Идентификаторы иллюстраций")
    private List<String> imageIds;


}
