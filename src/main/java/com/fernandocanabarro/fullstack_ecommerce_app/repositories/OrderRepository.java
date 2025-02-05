package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{

    @Query("SELECT obj FROM Order obj WHERE obj.moment BETWEEN :minDate AND :maxDate")
    Page<Order> findByMomentBetween(LocalDateTime minDate, LocalDateTime maxDate,Pageable pageable);
}
