package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Cart;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByIdAndStatus(Long cartId, boolean status);

    List<Cart> findByUserAndStatus(User user, boolean status);

    Optional<Cart> findByUserAndProductAndStatus(User user, Product product, boolean status);

    //  delete cart by cartId
    void deleteById(Long cartId);

    @Query("SELECT c FROM Cart c WHERE c.user = :user AND c.status = :status")
    Page<Cart> findByUserCart(
            @Param("user") User user,
            @Param("status") boolean status,
            Pageable pageable
    );


}
