package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{

    @Query("SELECT obj FROM Category obj WHERE LOWER(TRIM(obj.name)) LIKE :name")
    Optional<Category> findByNameIgnoreCase(String name);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
