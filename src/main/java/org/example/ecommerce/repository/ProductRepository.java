package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    Optional<Product> findByNameAndStatus(String name, boolean status);

    @Query(
            "SELECT p FROM Product p " +
                    "WHERE (:categoryId IS NULL OR p.category.id = :categoryId) " +
                    "AND (:status IS NULL OR p.status = :status)"
    )
    Page<Product> findProducts(
            @Param("categoryId") Integer categoryId,
            @Param("status") Boolean status,
            Pageable pageable
    );

}