package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.DiscountCoupon;

@Repository
public interface DiscountCouponRepository extends JpaRepository<DiscountCoupon,Long> {

    Optional<DiscountCoupon> findByCode(String code);
    Page<DiscountCoupon> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
}
