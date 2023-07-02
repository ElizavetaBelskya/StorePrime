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
import ru.tinkoff.storePrime.dto.CustomerDto;
import ru.tinkoff.storePrime.dto.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;


import javax.annotation.security.PermitAll;
import javax.validation.Valid;

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
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    ResponseEntity<CustomerDto> updateCustomer(
            @Parameter(description = "Идентификатор покупателя", example = "1642") @PathVariable("id") Long customerId,
            @Valid @RequestBody NewOrUpdateCustomerDto updatedCustomer);


    @Operation(summary = "Получение аккаунта покупателя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация об аккаунте покупателя",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerDto.class))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Сведения об ошибке",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class))
                    }
            )
    })
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping
    ResponseEntity<CustomerDto> getThisCustomer(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl);


    @Operation(summary = "Удаление аккаунта покупателя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Аккаунт покупателя удален"),
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
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping
    ResponseEntity<CustomerDto> deleteCustomer(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl);


}
