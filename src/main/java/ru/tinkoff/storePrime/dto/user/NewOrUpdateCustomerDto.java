package ru.tinkoff.storePrime.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
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

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Фамилия", example = "Иванов")
    private String surname;

    @NotNull
    @Schema(description = "Пол", example = "MALE")
    private Customer.Gender gender;

    @NotNull
    @Past
    @Schema(description = "Дата рождения", example = "2000-01-01")
    private LocalDate birthdayDate;

    private AddressDto addressDto;

    @NotBlank
    @Size(min = 10, max = 100)
    private String passwordHash;


}
