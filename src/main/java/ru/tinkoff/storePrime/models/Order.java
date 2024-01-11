package ru.tinkoff.storePrime.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.models.base.LongIdEntity;
import ru.tinkoff.storePrime.models.user.Customer;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
