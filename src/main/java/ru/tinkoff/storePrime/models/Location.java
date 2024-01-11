package ru.tinkoff.storePrime.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Location {

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

}
