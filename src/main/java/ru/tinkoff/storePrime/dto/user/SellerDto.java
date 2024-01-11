package ru.tinkoff.storePrime.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.location.LocationDto;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Продавец")
public class SellerDto extends AccountDto {

    @Schema(description = "Название компании", example = "Tinkoff")
    private String name;

    @Schema(description = "Описание компании", example = "Наша компания является лидером на рынке")
    private String description;

    private LocationDto locationDto;

}
