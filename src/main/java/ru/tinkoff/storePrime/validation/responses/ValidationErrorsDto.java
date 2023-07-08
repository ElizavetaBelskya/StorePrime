package ru.tinkoff.storePrime.validation.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.storePrime.exceptions.ExceptionMessages;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationErrorsDto {

    @Schema(description = "Ошибки валидации")
    private List<ValidationErrorDto> errors;

    @Schema(description = "Константа, обозначающая вид ошибки", example = "VALIDATION_REJECTED")
    private final String serviceMessage = ExceptionMessages.VALIDATION_REJECTED;

}
