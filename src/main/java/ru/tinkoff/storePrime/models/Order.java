package ru.tinkoff.storePrime.models;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.storePrime.models.user.Customer;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "cart_item")
public class Order {

    public enum Status {
        CREATED, TRANSITING, DELIVERED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Product product;
    private Integer amount;
    private Status status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

}
