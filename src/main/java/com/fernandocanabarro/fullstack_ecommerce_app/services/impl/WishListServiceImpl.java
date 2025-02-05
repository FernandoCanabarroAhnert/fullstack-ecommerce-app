package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductWishListItemDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Product;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.WishList;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ProductRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.WishListRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.WishListService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductWishListItemDTO> getConnectedUserWishList() {
        return authService.getConnectedUser().getWishList()
            .getItems().stream().map(ProductWishListItemDTO::new)
            .toList();
    }

    @Override
    @Transactional
    public void addProductToWishList(Long productId) {
        User user = authService.getConnectedUser();
        WishList wishList = user.getWishList();
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        wishList.addProduct(product);
        wishListRepository.save(wishList);
    }

    @Override
    @Transactional
    public void removeProductFromWishList(Long productId) {
        User user = authService.getConnectedUser();
        WishList wishList = user.getWishList();
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        wishList.removeProduct(product);
        wishListRepository.save(wishList);
    }


}
