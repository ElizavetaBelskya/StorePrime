package ru.tinkoff.storePrime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tinkoff.storePrime.models.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> getOrdersByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o JOIN o.product p WHERE p.seller.id = :sellerId")
    List<Order> getOrdersByProductsSellerId(Long sellerId);

    List<Order> findByCustomer_IdAndStatus(Long customerId, Order.Status status);

}
