package dev.fernando.dscatalog.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscatalog.dto.CategoryDTO;
import dev.fernando.dscatalog.entities.Category;
import dev.fernando.dscatalog.repositories.CategoryRepository;
import dev.fernando.dscatalog.services.exceptions.DatabaseException;
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
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        return this.categoryRepository.findAll(pageable).map(CategoryDTO::new);
    }
    protected Category findEntityById(Long id) {
        return this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria de Id = %d não encontrada!".formatted(id)));
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(this.findEntityById(id));
    }
    @Transactional
    public CategoryDTO store(CategoryDTO dto) {
        dto.setId(null);
        return new CategoryDTO(this.categoryRepository.save(new Category(dto)));
    }
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category entity = this.findEntityById(id);
        entity.setName(dto.getName());
        return new CategoryDTO(this.categoryRepository.save(entity));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        Category entity = this.findEntityById(id);
        try {
            this.categoryRepository.delete(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir a categoria de id = %d, pois a mesma possui vínculos!".formatted(id));
        }
    }
}
