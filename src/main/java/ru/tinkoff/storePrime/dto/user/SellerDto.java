package ru.tinkoff.storePrime.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.location.LocationDto;
import ru.tinkoff.storePrime.models.user.Seller;

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

    public static SellerDto from(Seller seller) {
        return SellerDto.builder()
                .id(seller.getId())
                .email(seller.getEmail())
                .phoneNumber(seller.getPhoneNumber())
                .cardBalance(seller.getCardBalance())
                .name(seller.getName())
                .description(seller.getDescription())
                .locationDto(LocationDto.from(seller.getLocation()))
                .build();
    }


}
