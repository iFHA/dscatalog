package dev.fernando.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.fernando.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
