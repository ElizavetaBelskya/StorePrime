package ru.tinkoff.storePrime.models.base;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@SuperBuilder
@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class LongIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unique_id_in_all_db")
    private Long id;

}
