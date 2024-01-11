package ru.tinkoff.storePrime.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.dto.product.CategoryDto;

import java.util.List;

@Tags(value = {
        @Tag(name = "Category")
})
@RequestMapping("/category")
public interface CategoryApi {

    @Operation(summary = "Получение всего списка категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категории найдены",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Категории не найдены",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                }
            )
    })
    @GetMapping("/all")
    ResponseEntity<List<CategoryDto>> getAllCategories();


}
