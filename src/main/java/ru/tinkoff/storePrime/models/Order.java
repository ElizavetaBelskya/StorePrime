package ru.tinkoff.storePrime.models;


import javax.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.models.base.LongIdEntity;
import ru.tinkoff.storePrime.models.user.Customer;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "market_order")
public class Order extends LongIdEntity {

    public enum Status {
        CREATED, TRANSITING, DELIVERED, CANCELLED
    }

    @ManyToOne
    private Product product;

    private Integer quantity;

    private Status status;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

}
