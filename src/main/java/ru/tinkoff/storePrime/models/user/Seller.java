package ru.tinkoff.storePrime.models.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.NewOrUpdateSellerDto;
import ru.tinkoff.storePrime.dto.SellerDto;
import ru.tinkoff.storePrime.models.Location;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data

@Entity
@Table(name = "seller")
public class Seller extends Account {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    private String INN;

    @Embedded
    private Location location;

}
