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
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.dto.user.CustomerDto;
import ru.tinkoff.storePrime.dto.user.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;

@Tags(value = {
        @Tag(name = "Customer")
})
@RequestMapping("/customer")
public interface CustomerApi {

    @Operation(summary = "Добавление аккаунта покупателя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный аккаунт покупателя",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "400", description = "Данные некорректны",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PostMapping
    @PermitAll
    ResponseEntity<CustomerDto> addCustomer(
            @Valid @RequestBody NewOrUpdateCustomerDto newCustomer);

    @Operation(summary = "Обновление аккаунта покупателя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновленный аккаунт покупателя",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке: пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке: пользователь не авторизован",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<CustomerDto> updateCustomerCardBalance(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody NewOrUpdateCustomerDto updatedCustomer);


    @Operation(summary = "Получение аккаунта покупателя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация об аккаунте покупателя",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке: пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке: пользователь не авторизован",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<CustomerDto> getThisCustomer(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl);


    @Operation(summary = "Удаление аккаунта покупателя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Аккаунт удален",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema)
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке: пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке: пользователь не авторизован",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<Void> deleteCustomer(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl);


    @Operation(summary = "Пополнение счета покупателя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновленный аккаунт покупателя",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке: пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке: пользователь не авторизован",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PatchMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<CustomerDto> updateCustomerCardBalance(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                          @RequestBody @DecimalMin("0.0") Double replenishment);




}
