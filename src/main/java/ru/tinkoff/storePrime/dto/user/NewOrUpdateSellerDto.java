package ru.tinkoff.storePrime.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.location.LocationDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Продавец")
public class NewOrUpdateSellerDto {

    @NotBlank
    @Email
    @Size(max = 100)
    @Schema(description = "Электронная почта", example = "example@mail.ru")
    private String email;

    @NotBlank
    @Size(max = 12)
    @Schema(description = "Номер телефона", example = "899999999")
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Имя", example = "Иван")
    private String name;

    @Schema(description = "Описание компании", example = "Наша компания является лидером на рынке")
    @Size(max = 250)
    private String description;

    private LocationDto locationDto;

    @Schema(description = "Счет ИНН", example = "54678912")
    private String INN;

}
