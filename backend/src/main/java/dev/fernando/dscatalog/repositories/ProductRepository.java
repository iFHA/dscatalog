package dev.fernando.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.fernando.dscatalog.entities.Product;
import dev.fernando.dscatalog.projections.ProductProjection;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(
        value = """
        select distinct a.id, a.name 
        from tb_product a 
            join tb_product_category b on b.product_id=a.id
        where 
            lower(a.name) like lower(concat('%', :name, '%'))
            and (:categoryId IS NULL OR b.category_id in :categoryId)
                """,
        countQuery = """
        select count(distinct a.id)
        from tb_product a 
            join tb_product_category b on b.product_id=a.id
        where 
        lower(a.name) like lower(concat('%', :name, '%'))
            and (:categoryId IS NULL OR b.category_id in :categoryId)
                """,
        nativeQuery = true
    )
    Page<ProductProjection> searchAllProducts(List<Long> categoryId, String name, Pageable pageable);
    
    @Query(value = "select p from Product p join fetch categories where p.id in :productId")
    List<Product> searchProductsWithCategories(List<Long> productId);
}
