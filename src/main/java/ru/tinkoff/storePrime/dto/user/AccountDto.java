package ru.tinkoff.storePrime.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.base.LongIdDto;


@SuperBuilder
@Data
@NoArgsConstructor
public abstract class AccountDto extends LongIdDto {

    @Schema(description = "Электронная почта", example = "example@mail.ru")
    private String email;

    @Schema(description = "Номер телефона", example = "899999999")
    private String phoneNumber;

    @Schema(description = "Баланс счета", example = "550")
    private Double cardBalance;


}
