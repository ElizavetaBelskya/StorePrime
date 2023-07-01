package ru.tinkoff.storePrime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.storePrime.models.user.Customer;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);


}
