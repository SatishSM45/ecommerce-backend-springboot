package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Page<Address> findByUser_Id(Long userId, Pageable pageable);

    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

    Optional<Address> findByUser_IdAndIsDefault(Long userId, boolean defaultAddress);
}
