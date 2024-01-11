package ru.tinkoff.storePrime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tinkoff.storePrime.models.CartItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCustomer_IdAndProduct_Id(Long customer_id, Long product_id);

    List<CartItem> findByCustomer_Id(Long customerId);
}
