package ru.tinkoff.storePrime.dto.cart;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.converters.ProductConverter;
import ru.tinkoff.storePrime.dto.base.LongIdDto;
import ru.tinkoff.storePrime.dto.product.ProductDto;
import ru.tinkoff.storePrime.models.CartItem;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Schema(description = "Товар из корзины")
public class CartItemDto extends LongIdDto {

    private ProductDto product;

    @Schema(description = "Идентификатор пользователя", example = "123")
    private Long customerId;

    @Schema(description = "Количество товара в корзине", example = "3")
    private Integer quantity;

    public static CartItemDto from(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .product(ProductConverter.getProductDtoFromProduct(cartItem.getProduct()))
                .customerId(cartItem.getCustomer().getId())
                .quantity(cartItem.getQuantity())
                .build();
    }

    public static List<CartItemDto> from(List<CartItem> cartItems) {
        return cartItems.stream().map(CartItemDto::from)
                .collect(Collectors.toList());
    }


}
