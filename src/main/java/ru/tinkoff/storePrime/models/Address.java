package ru.tinkoff.storePrime.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

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

    @Column
    private String apartment;


}
