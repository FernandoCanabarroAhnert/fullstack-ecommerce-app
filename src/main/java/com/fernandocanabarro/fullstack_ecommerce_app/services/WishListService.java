package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductWishListItemDTO;

public interface WishListService {

    List<ProductWishListItemDTO> getConnectedUserWishList();

    void addProductToWishList(Long productId);

    void removeProductFromWishList(Long productId);
}
