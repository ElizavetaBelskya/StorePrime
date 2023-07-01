package ru.tinkoff.storePrime.models.user;


import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.models.Address;
import ru.tinkoff.storePrime.models.CartItem;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data

@Entity
@Table(name = "customer")
public class Customer extends Account {

    public enum Gender {
        MALE, FEMALE
    }


    private String name;

    private String surname;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private LocalDate birthdayDate;

    @OneToMany(mappedBy = "customer")
    private List<CartItem> cart;

    @Embedded
    private Address address;


}
