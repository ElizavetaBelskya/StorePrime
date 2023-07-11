package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.dto.product.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories();

}
