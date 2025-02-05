package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Product;
import com.fernandocanabarro.fullstack_ecommerce_app.projections.ProductProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT MAX(p.price) FROM Product p")
    BigDecimal findMaxPrice();

    @Query(nativeQuery = true,value = """ 
        SELECT * FROM (
        SELECT DISTINCT p.id,p.name
        FROM products AS p
        INNER JOIN product_category AS pc ON pc.product_id = p.id
        INNER JOIN brands ON p.brand_id = brands.id
        WHERE (:categoryIds IS NULL OR pc.category_id IN (:categoryIds))
        AND (:brandsIds IS NULL OR brands.id IN (:brandsIds))
        AND p.price BETWEEN :minPrice AND :maxPrice
        AND LOWER(p.name) LIKE LOWER(CONCAT('%',:name,'%'))
        ) AS result
         """,
        countQuery = """
        SELECT COUNT(*) FROM (
        SELECT DISTINCT p.id,p.name
        FROM products AS p
        INNER JOIN product_category AS pc ON pc.product_id = p.id
        INNER JOIN brands ON p.brand_id = brands.id
        WHERE (:categoryIds IS NULL OR pc.category_id IN :categoryIds)
        AND (:brandsIds IS NULL OR brands.id IN (:brandsIds))
        AND p.price BETWEEN :minPrice AND :maxPrice
        AND LOWER(p.name) LIKE LOWER(CONCAT('%',:name,'%'))
        ) AS result 
        """
    )
    Page<ProductProjection> searchProducts(List<Long> categoryIds, List<Long> brandsIds, BigDecimal minPrice, BigDecimal maxPrice, String name,Pageable pageable);

    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id IN :productIds")
    List<Product> searchProductsWithCategories(List<Long> productIds);

    @Query(nativeQuery = true, value = "SELECT * from products WHERE is_in_offer = true LIMIT 10")
    List<Product> findProductsInOffer();
}
