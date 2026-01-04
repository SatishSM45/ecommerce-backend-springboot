package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser_Id(Long userId, Pageable pageable);

    Optional<Order> findByOrderIdAndUser(String orderId, User user);

    Optional<Order> findByOrderId(String orderId);
}
