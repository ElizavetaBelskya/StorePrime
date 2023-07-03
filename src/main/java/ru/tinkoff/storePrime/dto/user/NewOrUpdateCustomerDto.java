package ru.tinkoff.storePrime.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.tinkoff.storePrime.dto.location.AddressDto;
import ru.tinkoff.storePrime.models.user.Customer;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Покупатель")
public class NewOrUpdateCustomerDto {

    @Schema(description = "Электронная почта", example = "example@mail.ru")
    private String email;

    @Schema(description = "Номер телефона", example = "899999999")
    private String phoneNumber;

    @Schema(description = "Имя", example = "Иван")
    private String name;

    @Schema(description = "Фамилия", example = "Иванов")
    private String surname;

    @Schema(description = "Пол", example = "MALE")
    private Customer.Gender gender;

    @Schema(description = "Дата рождения", example = "2000-01-01")
    private LocalDate birthdayDate;

    private AddressDto addressDto;

    private String passwordHash;


}
