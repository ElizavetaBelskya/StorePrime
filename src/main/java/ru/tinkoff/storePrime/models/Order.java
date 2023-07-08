package ru.tinkoff.storePrime.models;


import javax.persistence.*;

import lombok.*;
import ru.tinkoff.storePrime.models.user.Customer;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "market_order")
public class Order {

    public enum Status {
        CREATED, TRANSITING, DELIVERED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Product product;

    private Integer quantity;

    private Status status;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

}
