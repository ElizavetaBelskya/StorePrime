package ru.tinkoff.storePrime.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.dto.product.CategoryDto;
import ru.tinkoff.storePrime.repository.CategoryRepository;
import ru.tinkoff.storePrime.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(x -> new CategoryDto(x.getName(), x.getImageId())).collect(Collectors.toList());
    }
}
