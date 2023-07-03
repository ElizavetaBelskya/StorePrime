package ru.tinkoff.storePrime.dto.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.storePrime.models.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Адрес")
public class AddressDto {


    @Schema(description = "Улица", example = "Спартаковская")
    private String street;

    @Schema(description = "Дом", example = "98")
    private Integer house;

    @Schema(description = "Квартира/Корпус", example = "6А")
    private String apartment;

    private LocationDto location;

    public static AddressDto from(Address address) {
        return AddressDto.builder()
                .street(address.getStreet())
                .house(address.getHouse())
                .apartment(address.getApartment())
                .location(LocationDto.from(address.getLocation())).build();
    }

}
