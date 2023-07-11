package ru.tinkoff.storePrime.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.base.LongIdDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Категория")
public class CategoryDto {

    @Schema(description = "Название категории", example = "Посуда")
    private String name;

    @Schema(description = "Идентификатор иллюстрации", example = "1233332sqw")
    private String imageId;

}
