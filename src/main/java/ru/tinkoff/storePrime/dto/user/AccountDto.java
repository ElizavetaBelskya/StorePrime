package ru.tinkoff.storePrime.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
@NoArgsConstructor
public abstract class AccountDto {

    @Schema(description = "Идентификатор", example = "193")
    private Long id;

    @Schema(description = "Электронная почта", example = "example@mail.ru")
    private String email;

    @Schema(description = "Номер телефона", example = "899999999")
    private String phoneNumber;

    @Schema(description = "Баланс счета", example = "550")
    private Double cardBalance;


}
