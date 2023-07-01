package ru.tinkoff.storePrime.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.tinkoff.storePrime.models.Location;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Местоположение")
public class LocationDto {

    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Город", example = "Москва")
    private String city;

    public static LocationDto from(Location location) {
        return LocationDto.builder()
                .country(location.getCountry())
                .city(location.getCity())
                .build();
    }

}
