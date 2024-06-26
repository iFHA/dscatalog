package dev.fernando.dscatalog.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscatalog.dto.CategoryDTO;
import dev.fernando.dscatalog.entities.Category;
import dev.fernando.dscatalog.repositories.CategoryRepository;
import dev.fernando.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return this.categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria de Id = %d não encontrada!".formatted(id))));
    }
    @Transactional
    public CategoryDTO store(CategoryDTO dto) {
        dto.setId(null);
        return new CategoryDTO(this.categoryRepository.save(new Category(dto)));
    }
}
