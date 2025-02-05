package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long>{

    @Query("SELECT obj FROM ProductImage obj WHERE obj.product.id = :productId")
    List<ProductImage> findProductImagesByProductId(Long productId);
}
