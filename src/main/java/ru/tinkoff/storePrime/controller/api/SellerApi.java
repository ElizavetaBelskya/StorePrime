package ru.tinkoff.storePrime.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tinkoff.storePrime.dto.CustomerDto;
import ru.tinkoff.storePrime.dto.NewOrUpdateCustomerDto;
import ru.tinkoff.storePrime.dto.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.SellerDto;

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

}
