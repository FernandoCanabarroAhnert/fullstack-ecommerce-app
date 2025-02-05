package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductCartItemResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Cart;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Product;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductCartItem;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.pk.ProductCartItemPK;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.CartRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ProductCartItemRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ProductRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CartService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ProductCartItemRepository productCartItemRepository;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductCartItemResponseDTO> getConnectedUserCart() {
        return authService.getConnectedUser().getCart()
            .getItems().stream().map(ProductCartItemResponseDTO::new)
            .toList();
    }

    @Override
    @Transactional
    public void addProductToCart(Long productId, Integer quantity) {
        User user = authService.getConnectedUser();
        Cart cart = user.getCart();
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        ProductCartItemPK id = new ProductCartItemPK(product, cart);
        ProductCartItem productCartItem = productCartItemRepository.findById(id)
            .orElse(null);

        if (productCartItem != null) {
            productCartItem.setQuantity(productCartItem.getQuantity() + quantity);
        }
        else {
            productCartItem = new ProductCartItem(product, cart, quantity, 
                product.getIsInOffer() ? product.getOfferPrice() : product.getPrice());
        }

        productCartItemRepository.save(productCartItem);
        cart.addItem(productCartItem);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void updateProductQuantity(Long productId, Integer quantity, String option) {
        User user = authService.getConnectedUser();
        Cart cart = user.getCart();
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        ProductCartItemPK id = new ProductCartItemPK(product, cart);
        ProductCartItem productCartItem = productCartItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado no Carrinho"));
        
        if (option.equals("increase")) {
            productCartItem.setQuantity(productCartItem.getQuantity() + quantity);
        }   
        else {
            productCartItem.setQuantity(productCartItem.getQuantity() - quantity);
        }
        
        productCartItemRepository.save(productCartItem);
    }

    @Override
    @Transactional
    public void deleteProductFromCart(Long productId) {
        User user = authService.getConnectedUser();
        Cart cart = user.getCart();
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        ProductCartItemPK id = new ProductCartItemPK(product, cart);
        ProductCartItem productCartItem = productCartItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado no Carrinho"));

        cart.removeItem(productCartItem);
        productCartItemRepository.deleteById(productCartItem.getId());
    }

}
