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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.dto.user.AccountDto;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.SellerDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

@Tags(value = {
        @Tag(name = "Accounts")
})
@RequestMapping("/accounts")
public interface AccountApi {


    @Operation(summary = "Получение аккаунта пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация об аккаунте покупателя",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "200", description = "Информация об аккаунте продавца",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SellerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке: пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
            @ApiResponse(responseCode = "403", description = "Сведения об ошибке: доступ запрещен",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'SELLER')")
    ResponseEntity<AccountDto> getThisAccount(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl);

}
