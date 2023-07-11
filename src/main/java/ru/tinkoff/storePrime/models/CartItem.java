package ru.tinkoff.storePrime.models;


import javax.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.models.base.LongIdEntity;
import ru.tinkoff.storePrime.models.user.Customer;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "cart_item")
public class CartItem extends LongIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

}
