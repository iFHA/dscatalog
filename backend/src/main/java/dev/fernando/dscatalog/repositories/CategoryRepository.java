package dev.fernando.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.fernando.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
