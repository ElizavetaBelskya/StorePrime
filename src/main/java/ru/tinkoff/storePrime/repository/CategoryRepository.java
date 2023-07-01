package ru.tinkoff.storePrime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.storePrime.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
