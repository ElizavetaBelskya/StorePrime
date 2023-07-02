package ru.tinkoff.storePrime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.storePrime.models.CartItem;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCustomer_IdAndProduct_Id(Long customer_id, Long product_id);

}
