package ru.tinkoff.storePrime.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Информация об ошибке")
public class ExceptionDto {

    @Schema(description = "Сообщение", example = "Ресурс не найден")
    private String message;
    @Schema(description = "HTTP-код", example = "404")
    private int status;
    @Schema(description = "Константа, обозначающая вид ошибки", example = "CUSTOMER_NOT_FOUND")
    private String serviceMessage;


}
