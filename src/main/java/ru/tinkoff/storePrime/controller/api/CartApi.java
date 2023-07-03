package ru.tinkoff.storePrime.controller.api;


import io.swagger.v3.oas.annotations.Operation;
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
import ru.tinkoff.storePrime.dto.CartItemDto;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

import javax.validation.Valid;
import java.util.List;

@Tags(value = {
        @Tag(name = "Carts")
})
@RequestMapping("/carts")
public interface CartApi {

    @Operation(summary = "Добавление или изменение количества товара в корзину")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный в корзину товар",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CartItemDto.class))
                    }
            )
    })
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/{productId}")
    ResponseEntity<CartItemDto> addProductToCart(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                 @PathVariable("productId") Long productId,
                                                 @RequestParam("quantity") Integer quantity);


    @Operation(summary = "Получение всех товаров в корзине пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный в корзину товар",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CartItemDto.class))
                    }
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<List<CartItemDto>> getCustomerCart(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl);


    @Operation(summary = "Удаление товара из корзины")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар удален из корзины",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CartItemDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Товар не найден в корзине",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("/{productId}")
    ResponseEntity<CartItemDto> deleteProductFromCart(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                 @PathVariable("productId") Long productId);


}
