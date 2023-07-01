package ru.tinkoff.storePrime.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.storePrime.dto.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.ProductDto;

import javax.validation.Valid;
import java.util.List;

@Tags(value = {
        @Tag(name = "Products")
})
@RequestMapping("/products")
public interface ProductApi {

    @Operation(summary = "Добавление товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный товар",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            )
    })
    @PostMapping
    ResponseEntity<ProductDto> addProduct(
            @Valid @RequestBody NewOrUpdateProductDto newProduct);


    @Operation(summary = "Получение товара по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id);

    @Operation(summary = "Получение товаров по идентификатору продавца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товары найдены",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Товары не найдены")
    })
    @GetMapping("/seller/{sellerId}")
    ResponseEntity<List<ProductDto>> getProductsBySellerId(@PathVariable("sellerId") Long sellerId);

    @Operation(summary = "Обновление товара по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар обновлен",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @PutMapping("/{id}")
    ResponseEntity<ProductDto> updateProductById(@PathVariable("id") Long id,
                                                 @Valid @RequestBody NewOrUpdateProductDto updatedProduct);


}
