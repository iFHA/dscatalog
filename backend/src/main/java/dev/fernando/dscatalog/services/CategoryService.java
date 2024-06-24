package dev.fernando.dscatalog.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.fernando.dscatalog.dto.CategoryDTO;
import dev.fernando.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public List<CategoryDTO> findAll() {
        return this.categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }
}
