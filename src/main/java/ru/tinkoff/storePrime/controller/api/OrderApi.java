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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tinkoff.storePrime.dto.cart.CartItemDto;
import ru.tinkoff.storePrime.dto.order.OrderDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

import java.util.List;

@Tags(value = {
        @Tag(name = "Orders")
})
@RequestMapping("/orders")
public interface OrderApi {

    @Operation(summary = "Создание ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный в корзину товар",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDto.class))
                    }
            )
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                         @RequestBody List<Long> pa);



}
