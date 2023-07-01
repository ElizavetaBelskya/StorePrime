package ru.tinkoff.storePrime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tinkoff.storePrime.models.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.seller.id = :sellerId")
    List<Product> findAllBySellerId(@Param("sellerId") Long sellerId);
}
