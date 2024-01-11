package ru.tinkoff.storePrime.models;


import lombok.*;
import ru.tinkoff.storePrime.models.base.LongIdEntity;

import javax.persistence.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "category")
public class Category extends LongIdEntity {


    @Column(nullable = false, unique = true)
    private String name;

    private String imageId;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<Product> products;


}
