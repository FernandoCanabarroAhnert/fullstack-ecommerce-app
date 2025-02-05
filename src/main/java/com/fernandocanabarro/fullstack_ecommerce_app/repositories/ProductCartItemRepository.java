package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductCartItem;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.pk.ProductCartItemPK;

@Repository
public interface ProductCartItemRepository extends JpaRepository<ProductCartItem,ProductCartItemPK>{

}
