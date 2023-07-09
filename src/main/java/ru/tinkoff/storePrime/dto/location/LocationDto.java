package ru.tinkoff.storePrime.dto.location;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.tinkoff.storePrime.models.Location;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Местоположение")
public class LocationDto {

    @Schema(description = "Страна", example = "Россия")
    @NotBlank(message = "{location.country.notBlank}")
    private String country;

    @Schema(description = "Город", example = "Москва")
    @NotBlank(message = "{location.city.notBlank}")
    private String city;

    public static LocationDto from(Location location) {
        return LocationDto.builder()
                .country(location.getCountry())
                .city(location.getCity())
                .build();
    }

}

