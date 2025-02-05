package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductCartItemResponseDTO;

public interface CartService {

    List<ProductCartItemResponseDTO> getConnectedUserCart();

    void addProductToCart(Long productId, Integer quantity);

    void updateProductQuantity(Long productId, Integer quantity, String option);

    void deleteProductFromCart(Long productId);
}
