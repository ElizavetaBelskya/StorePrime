package ru.tinkoff.storePrime.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.dto.product.NewOrUpdateProductDto;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.dto.product.ProductsPage;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
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
            ),
            @ApiResponse(responseCode = "403", description = "Сведения об ошибке: доступ запрещен",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PostMapping
    @PreAuthorize("hasAuthority('SELLER')")
    ResponseEntity<ProductDto> addProduct(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
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
            }),
            @ApiResponse(responseCode = "400", description = "Сведения об ошибке: неверный запрос",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id);

    @Operation(summary = "Получение случайного товара")
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
                    }),
            @ApiResponse(responseCode = "400", description = "Сведения об ошибке: неверный запрос",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/random")
    ResponseEntity<ProductDto> getRandomProduct();

    @Operation(summary = "Получение случайных товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товары найдены",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Товары не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Сведения об ошибке: неверный запрос",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/random/{amount}")
    ResponseEntity<List<ProductDto>> getRandomProducts(@PathVariable("amount") Integer amount);

    @Operation(summary = "Получение товаров по идентификатору продавца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товары найдены",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Товары не найдены",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Сведения об ошибке: неверный запрос",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
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
                    }),
            @ApiResponse(responseCode = "403", description = "Сведения об ошибке: доступ запрещен",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
            @ApiResponse(responseCode = "400", description = "Сведения об ошибке: неверный запрос",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SELLER')")
    ResponseEntity<ProductDto> updateProductById(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
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
                    }),
            @ApiResponse(responseCode = "403", description = "Сведения об ошибке: доступ запрещен",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
            @ApiResponse(responseCode = "400", description = "Сведения об ошибке: неверный запрос",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SELLER')")
    ResponseEntity<Void> deleteProductById(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                 @PathVariable("id") Long productId);



    @Operation(summary = "Получение списка товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница с товарами",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductsPage.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Сведения об ошибке: неверный запрос",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/pages")
    ResponseEntity<ProductsPage> getProducts(
            @Parameter(description = "Номер страницы", example = "1") @Min(1) @RequestParam("page") int page,
            @Parameter(description = "Минимальная стоимость товара", example = "1500") @Nullable @DecimalMin("0") @RequestParam(value = "minPrice", required = false) Double minPrice,
            @Parameter(description = "Максимальная стоимость товара", example = "1500") @Nullable @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @Parameter(description = "Категория товара", example = "pets") @Nullable @Pattern(regexp = "[a-zA-Z]+") @RequestParam(value = "category", required = false) String category,
            @Parameter(description = "Идентификатор продавца", example = "1") @Nullable @Min(1) @RequestParam(value = "sellerId", required = false) Long sellerId
    );

    @Operation(summary = "Получение списка товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница с товарами",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Сведения об ошибке: неверный запрос",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping
    ResponseEntity<List<ProductDto>> getAllProducts(
            @Parameter(description = "Минимальная стоимость товара", example = "1500") @Nullable @RequestParam(value = "minPrice", required = false) Double minPrice,
            @Parameter(description = "Максимальная стоимость товара", example = "1500") @Nullable @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @Parameter(description = "Категория товара", example = "pets") @Nullable @RequestParam(value = "category", required = false) String category,
            @Parameter(description = "Идентификатор продавца", example = "1") @Nullable @RequestParam(value = "id", required = false) Long sellerId
    );

    @Operation(summary = "Получение товаров по названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "список товаров по названию",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
                    }
            ),
            @ApiResponse(responseCode = "400", description = "Сведения об ошибке: неверный запрос",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/search")
    ResponseEntity<List<ProductDto>> getProductsByContentString(@Parameter(description = "Строка для поиска", example = "dress") @RequestParam("content") String content,
                                                                @Parameter(description = "Категория для поиска", example = "toys") @Nullable @RequestParam("category") String category);


}
