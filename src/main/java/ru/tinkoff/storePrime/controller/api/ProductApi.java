package ru.tinkoff.storePrime.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.storePrime.dto.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.ProductDto;
import ru.tinkoff.storePrime.dto.ProductsPage;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

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
    @PreAuthorize("hasAuthority('SELLER')")
    @PostMapping
    ResponseEntity<ProductDto> addProduct(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody NewOrUpdateProductDto newProduct);


    @Operation(summary = "Получение товара по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Товар не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
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
            @ApiResponse(responseCode = "404", description = "Товары не найдены",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
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
            @ApiResponse(responseCode = "404", description = "Товар не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PreAuthorize("hasAuthority('SELLER')")
    @PutMapping("/{id}")
    ResponseEntity<ProductDto> updateProductById(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable("id") Long id, @Valid @RequestBody NewOrUpdateProductDto updatedProduct);

    @Operation(summary = "Удаление товара по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар удален",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Товар не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PreAuthorize("hasAuthority('SELLER')")
    @DeleteMapping("/{id}")
    ResponseEntity<ProductDto> deleteProductById(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                 @PathVariable("id") Long productId);



    @Operation(summary = "Получение списка товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница с товарами",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductsPage.class))
                    })
    })
    @GetMapping("/pages")
    ResponseEntity<ProductsPage> getProducts(
            @Parameter(description = "Номер страницы", example = "1") @RequestParam("page") int page,
            @Parameter(description = "Минимальная стоимость товара", example = "?minPrice=1500") @RequestParam(value = "minPrice", required = false) Double minPrice,
            @Parameter(description = "Максимальная стоимость товара", example = "?maxPrice=1500") @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @Parameter(description = "Категория товара", example = "?category=pets") @RequestParam(value = "category", required = false) String category,
            @Parameter(description = "Идентификатор продавца", example = "1") @RequestParam(value = "id", required = false) Long sellerId
    );

    @Operation(summary = "Получение списка товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница с товарами",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))
                    })
    })
    @GetMapping
    ResponseEntity<List<ProductDto>> getAllProducts(
            @Parameter(description = "Минимальная стоимость товара", example = "?minPrice=1500") @RequestParam(value = "minPrice", required = false) Double minPrice,
            @Parameter(description = "Максимальная стоимость товара", example = "?maxPrice=1500") @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @Parameter(description = "Категория товара", example = "?category=pets") @RequestParam(value = "category", required = false) String category,
            @Parameter(description = "Идентификатор продавца", example = "1") @RequestParam(value = "id", required = false) Long sellerId
    );





}
