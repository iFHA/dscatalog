package dev.fernando.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscatalog.repositories.ProductRepository;
import dev.fernando.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService service;
    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(existingId);
        Assertions.assertEquals(countTotalProducts - 1L, repository.count());
    }

    @Test
    void deleteShouldThrowResourceNotFoundWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, ()->service.delete(nonExistingId));
    }

    @Test
    void findAllPagedShouldReturnPageWhenPage0Size10() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        var page = service.findAllPaged(pageRequest);

        Assertions.assertFalse(page.isEmpty());
        Assertions.assertEquals(0, page.getNumber());
        Assertions.assertEquals(10, page.getSize());
        Assertions.assertEquals(countTotalProducts, page.getTotalElements());
    }
    @Test
    void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExists() {
        PageRequest pageRequest = PageRequest.of(1, 25);
        var page = service.findAllPaged(pageRequest);

        Assertions.assertTrue(page.isEmpty());
    }
    @Test
    void findAllPagedShouldReturnSortedPageWhenSortByName() {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("name"));
        var page = service.findAllPaged(pageRequest);

        Assertions.assertFalse(page.isEmpty());
        Assertions.assertEquals("Macbook Pro", page.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", page.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", page.getContent().get(2).getName());
    }
}
