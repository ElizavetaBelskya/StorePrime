package ru.tinkoff.storePrime.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.location.LocationDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Продавец")
public class NewOrUpdateSellerDto {

    @NotBlank(message = "{seller.email.notBlank}")
    @Size(max = 100, message = "{seller.email.size}")
    @Email(message = "{seller.email.email}")
    @Pattern(regexp = "^[-\\w]+(\\.[-\\w]+)*@[A-Za-z0-9]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$", message = "{seller.email.pattern}")
    @Schema(description = "Электронная почта", example = "example@mail.ru")
    private String email;

    @NotBlank(message = "{seller.phoneNumber.notBlank}")
    @Size(max = 12, message = "{seller.phoneNumber.size}")
    @Pattern(regexp = "\\d{11}", message = "{seller.phoneNumber.pattern}")
    @Schema(description = "Номер телефона", example = "89999999999")
    private String phoneNumber;

    @NotBlank(message = "{seller.name.notBlank}")
    @Size(min = 1, max = 100, message = "{seller.name.size}")
    @Pattern(regexp = "^[^0-9!@#$%^&*()+=|{}\\[\\]<>?/~`\\s]+$", message = "{seller.name.pattern}")
    @Schema(description = "Название", example = "Тинькофф")
    private String name;

    @Schema(description = "Описание компании", example = "Наша компания является лидером на рынке")
    @Size(max = 250)
    private String description;

    private LocationDto locationDto;

    @NotBlank(message = "{seller.INN.notBlank}")
    @Pattern(regexp = "^\\d{10}$", message = "{seller.INN.pattern}")
    @Schema(description = "ИНН", example = "1234567890")
    private String INN;

    @NotBlank(message = "{seller.passwordHash.notBlank}")
    @Size(min = 8, max = 64, message = "{seller.passwordHash.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\^?!=.*\\[\\]\\\\/@$%#&_-])[A-Za-z\\d\\^?!=.*\\[\\]\\\\/@$%#&_-]+$", message = "{seller.passwordHash.pattern}")
    @Schema(description = "Пароль", example = "Password123!")
    private String passwordHash;

}

