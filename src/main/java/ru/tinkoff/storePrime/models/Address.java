package ru.tinkoff.storePrime.models;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.storePrime.dto.AddressDto;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {


    @Embedded
    private Location location;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private Integer house;

    @Column(nullable = true)
    private String apartment;


}
