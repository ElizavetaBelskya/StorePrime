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
import ru.tinkoff.storePrime.dto.user.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.user.SellerDto;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;

import javax.validation.Valid;

@Tags(value = {
        @Tag(name = "Seller")
})
@RequestMapping("/seller")
public interface SellerApi {

    @Operation(summary = "Добавление аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Добавленный аккаунт",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SellerDto.class))
                    }
            )
    })
    @PostMapping
    ResponseEntity<SellerDto> addSeller(
            @Valid @RequestBody NewOrUpdateSellerDto newSeller);


    @Operation(summary = "Обновление аккаунта продавца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Обновленный аккаунт продавца",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SellerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SellerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping
    @PreAuthorize("hasAuthority('SELLER')")
    ResponseEntity<SellerDto> updateSeller(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody NewOrUpdateSellerDto updatedSeller);


    @Operation(summary = "Получение аккаунта продавца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация об аккаунте продавца",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SellerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SellerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('SELLER')")
    ResponseEntity<SellerDto> getThisSeller(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl);

    @Operation(summary = "Получение аккаунта продавца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация об аккаунте продавца",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SellerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SellerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @GetMapping("/{id}")
    ResponseEntity<SellerDto> getSellerById(@Parameter(description = "Идентификатор покупателя", example = "1642")
            @PathVariable("id") Long id);


    @Operation(summary = "Удаление аккаунта продавца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Аккаунт продавца удален",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SellerDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @DeleteMapping
    @PreAuthorize("hasAuthority('SELLER')")
    ResponseEntity<Void> deleteSeller(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl);




}
