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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.storePrime.dto.cart.CartItemDto;
import ru.tinkoff.storePrime.dto.order.OrderDto;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

import java.util.List;

@Tags(value = {
        @Tag(name = "Orders")
})
@RequestMapping("/orders")
public interface OrderApi {

    @Operation(summary = "Создание заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Созданный заказ",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
                    }
            )
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<List<OrderDto>> createOrder(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                         @RequestBody List<Long> cartItemIdList);

    @Operation(summary = "Получение всех заказов пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказы пользователя",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
                    }
            )
    })
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<List<OrderDto>> getAllOrder(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl);

    @Operation(summary = "Получение всех заказов продавца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказы продавца",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
                    }
            )
    })
    @GetMapping("/all/seller")
    @PreAuthorize("hasAuthority('SELLER')")
    ResponseEntity<List<OrderDto>> getAllOrderForSeller(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl);

    @Operation(summary = "Получение всех отмененных заказов покупателя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказы продавца",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
                    }
            )
    })
    @GetMapping("/cancelled")
    @PreAuthorize("hasAuthority('СUSTOMER')")
    ResponseEntity<List<OrderDto>> getCancelledProductsForCustomer(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl);

    @Operation(summary = "Изменение статуса заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Измененный заказ",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDto.class))
                    }
            )
    })
    @PatchMapping("/status/{orderId}")
    @PreAuthorize("hasAuthority('SELLER')")
    ResponseEntity<OrderDto> changeStatus(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                          @PathVariable("orderId") Long orderId, @RequestParam("status") String status);

    @Operation(summary = "Изменение статуса заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Измененный заказ",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDto.class))
                    }
            )
    })
    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<OrderDto> cancelOrder(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PathVariable("orderId") Long orderId);



}
