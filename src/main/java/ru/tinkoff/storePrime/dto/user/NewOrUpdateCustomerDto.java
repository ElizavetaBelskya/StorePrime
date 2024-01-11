package ru.tinkoff.storePrime.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.tinkoff.storePrime.dto.location.AddressDto;
import ru.tinkoff.storePrime.models.user.Customer;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Покупатель")
public class NewOrUpdateCustomerDto {

    @NotBlank(message = "{customer.email.notBlank}")
    @Size(min = 1, max = 100, message = "{customer.email.size}")
    @Pattern(regexp = "^[-\\w]+(\\.[-\\w]+)*@[A-Za-z0-9]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$", message = "{customer.email.pattern}")
    @Schema(description = "Email", example = "example@mail.ru")
    private String email;

    @NotBlank(message = "{customer.phoneNumber.notBlank}")
    @Size(max = 12, message = "{customer.phoneNumber.size}")
    @Schema(description = "Номер телефона", example = "899999999")
    private String phoneNumber;

    @NotBlank(message = "{customer.name.notBlank}")
    @Size(min = 1, max = 100, message = "{customer.name.size}")
    @Pattern(regexp = "^[^0-9!@#$%^&*()+=|{}\\[\\]<>?/~`\\s]+$", message = "{customer.name.pattern}")
    @Schema(description = "Имя", example = "Иван")
    private String name;

    @NotBlank(message = "{customer.surname.notBlank}")
    @Size(min = 1, max = 100, message = "{customer.surname.size}")
    @Pattern(regexp = "^[^0-9!@#$%^&*()+=|{}\\[\\]<>?/~`\\s]+$", message = "{customer.surname.pattern}")
    @Schema(description = "Фамилия", example = "Иванов")
    private String surname;

    @NotNull(message = "{customer.gender.notNull}")
    @Schema(description = "Пол", example = "MALE")
    private Customer.Gender gender;

    @NotNull(message = "{customer.birthdayDate.notNull}")
    @Past(message = "{customer.birthdayDate.past}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "Дата рождения", example = "2000-12-01")
    private LocalDate birthdayDate;

    private AddressDto addressDto;

    @NotBlank(message = "{customer.passwordHash.notBlank}")
    @Size(min = 8, max = 64, message = "{customer.passwordHash.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\^?!=.*\\[\\]\\\\/@$%#&_-])[A-Za-z\\d\\^?!=.*\\[\\]\\\\/@$%#&_-]+$", message = "{customer.passwordHash.pattern}")
    @Schema(description = "Пароль", example = "Password123!")
    private String passwordHash;


}
