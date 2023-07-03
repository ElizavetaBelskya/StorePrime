package ru.tinkoff.storePrime.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.location.LocationDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Продавец")
public class NewOrUpdateSellerDto {

    @Schema(description = "Электронная почта", example = "example@mail.ru")
    private String email;

    @Schema(description = "Номер телефона", example = "899999999")
    private String phoneNumber;

    @Schema(description = "Название компании", example = "Tinkoff")
    private String name;

    @Schema(description = "Описание компании", example = "Наша компания является лидером на рынке")
    private String description;

    private LocationDto locationDto;

    @Schema(description = "Счет ИНН", example = "54678912")
    private String INN;

}
