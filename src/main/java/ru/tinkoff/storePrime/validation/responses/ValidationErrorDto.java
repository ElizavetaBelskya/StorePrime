package ru.tinkoff.storePrime.validation.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorDto {
    @Schema(description = "Наименование поля", example = "email")
    private String fieldName;
    @Schema(description = "Наименование объекта", example = "Account")
    private String objectName;
    @Schema(description = "Сообщение", example = "Некорректный email")
    private String message;
}
