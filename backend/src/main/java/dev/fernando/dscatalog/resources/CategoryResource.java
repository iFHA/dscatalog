package dev.fernando.dscatalog.resources;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fernando.dscatalog.dto.CategoryDTO;
import dev.fernando.dscatalog.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        return ResponseEntity.ok(
            this.categoryService.findAll()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
            this.categoryService.findById(id)
        );
    }
}
