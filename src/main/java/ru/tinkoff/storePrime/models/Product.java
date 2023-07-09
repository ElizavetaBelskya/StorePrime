package ru.tinkoff.storePrime.models;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.models.base.LongIdEntity;
import ru.tinkoff.storePrime.models.user.Seller;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter

@Entity
@Table(name = "product")
public class Product extends LongIdEntity {

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "price")
    private Double price;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @Column(name = "amount")
    private Integer amount;

    @ElementCollection
    private List<String> imagesIds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(this.getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId()) + 41*Objects.hash(title) +41*Objects.hash(description);
    }



}
