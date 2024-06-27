package dev.fernando.dscatalog.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscatalog.dto.ProductDTO;
import dev.fernando.dscatalog.entities.Product;
import dev.fernando.dscatalog.repositories.ProductRepository;
import dev.fernando.dscatalog.services.exceptions.DatabaseException;
import dev.fernando.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return this.productRepository.findAll().stream().map(ProductDTO::new).toList();
    }
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        return this.productRepository.findAll(pageable).map(ProductDTO::new);
    }
    protected Product findEntityById(Long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto de Id = %d não encontrada!".formatted(id)));
    }
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return new ProductDTO(this.findEntityById(id));
    }
    @Transactional
    public ProductDTO store(ProductDTO dto) {
        dto.setId(null);
        return new ProductDTO(this.productRepository.save(new Product(dto)));
    }
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product entity = this.findEntityById(id);
        copyDTOToEntity(dto, entity);
        return new ProductDTO(this.productRepository.save(entity));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        Product entity = this.findEntityById(id);
        try {
            this.productRepository.delete(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir o produto de id = %d, pois a mesma possui vínculos!".formatted(id));
        }
    }
    private void copyDTOToEntity(ProductDTO dto, Product entity) {
        entity.clearCategories();
        entity.setDate(dto.getDate());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        dto.getCategories().forEach(entity::addCategoryDTO);
    }
}
