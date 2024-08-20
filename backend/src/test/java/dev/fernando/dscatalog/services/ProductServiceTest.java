package dev.fernando.dscatalog.services;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.fernando.dscatalog.dto.ProductDTO;
import dev.fernando.dscatalog.entities.Product;
import dev.fernando.dscatalog.repositories.ProductRepository;
import dev.fernando.dscatalog.services.exceptions.DatabaseException;
import dev.fernando.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Product p1;
    private ProductDTO p1Dto;
    private PageImpl<Product> page;
    private List<Product> list;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        dependentId = 3L;
        nonExistingId = 1000L;
        p1 = new Product();
        p1Dto = new ProductDTO(p1);
        page = new PageImpl<>(List.of(p1));
        list = List.of(p1);
        
        when(repository.findAll()).thenReturn(list);
        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(ArgumentMatchers.any())).thenReturn(p1);

        when(repository.findById(existingId)).thenReturn(Optional.of(p1));
        when(repository.findById(dependentId)).thenReturn(Optional.of(p1));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        Mockito.verify(repository).findById(existingId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
        Mockito.verify(repository).findById(nonExistingId);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
        Mockito.verify(repository).deleteById(dependentId);
    }

    @Test
    void findAllShouldReturnList() {
        Assertions.assertNotNull(service.findAll());
        Mockito.verify(repository).findAll();
    }

    @Test
    void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Assertions.assertNotNull(service.findAllPaged("", "", pageable));
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    void findByIdShouldReturnDtoWhenIdExists() {
        Assertions.assertNotNull(service.findById(existingId));
        Assertions.assertInstanceOf(ProductDTO.class, service.findById(existingId));
        Mockito.verify(repository, Mockito.times(2)).findById(existingId);
    }
    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
        Mockito.verify(repository).findById(nonExistingId);
    }

    @Test
    void findEntityByIdShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.findEntityById(existingId);
        });
        Mockito.verify(repository).findById(existingId);
    }
    @Test
    void findEntityByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            service.findEntityById(nonExistingId);
        });
        Mockito.verify(repository).findById(nonExistingId);
    }

    @Test
    void storeShouldReturnDto() {
        Assertions.assertNotNull(service.store(p1Dto));
        Assertions.assertInstanceOf(ProductDTO.class, service.store(p1Dto));
        Mockito.verify(repository, Mockito.times(2)).save(p1);
    }

    @Test
    void updateShouldReturnDtoWhenIdExists() {
        Assertions.assertNotNull(service.update(existingId, p1Dto));
        Assertions.assertInstanceOf(ProductDTO.class, service.update(existingId, p1Dto));
        Mockito.verify(repository, Mockito.times(2)).save(p1);
    }
    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingId, p1Dto));
    }
}
