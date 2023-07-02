package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.models.Category;
import ru.tinkoff.storePrime.repository.CategoryRepository;
import ru.tinkoff.storePrime.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<String> getAllCategoryNames() {
        return categoryRepository.findAll().stream().map(Category::getName).collect(Collectors.toList());
    }
}
