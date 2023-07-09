package ru.tinkoff.storePrime.models;


import javax.persistence.*;
import lombok.*;
import ru.tinkoff.storePrime.dto.base.LongIdDto;
import ru.tinkoff.storePrime.models.base.LongIdEntity;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "category")
public class Category extends LongIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<Product> products;


}
