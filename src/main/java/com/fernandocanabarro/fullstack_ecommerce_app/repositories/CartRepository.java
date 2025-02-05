package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long>{

}
