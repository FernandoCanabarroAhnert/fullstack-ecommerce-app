package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductCartItemResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CartService;

@Controller
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/my-cart")
    public String getConnectedUserCart(Model model) {
        List<ProductCartItemResponseDTO> cartItems = cartService.getConnectedUserCart();
        String totalCartValue = String.valueOf(cartItems.stream().map(cartItem -> BigDecimal.valueOf(Double.parseDouble(cartItem.getSubTotal().replace(",", "."))))
            .reduce(BigDecimal.valueOf(0.0), (x,y) -> x.add(y))
            .setScale(2, RoundingMode.HALF_UP))
            .replace(".", ",");
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalCartValue", totalCartValue);
        return "site/cart";
    }

    @GetMapping("/{productId}/add/{quantity}")
    public String addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity, 
            RedirectAttributes redirectAttributes,@RequestHeader(value = "Referer", required = false) String referer) {
        cartService.addProductToCart(productId, quantity);
        redirectAttributes.addFlashAttribute("productHasBeenAddedToCart", true);
        return referer != null ? "redirect:" + referer : "redirect:/products-grid";
    }
    
    @GetMapping("/{productId}/increase")
    public String increaseProductQuantity(@PathVariable Long productId, @RequestParam Integer quantity) {
        cartService.updateProductQuantity(productId, quantity,"increase");
        return "redirect:/carts/my-cart";
    }

    @GetMapping("/{productId}/decrease")
    public String decreaseProductQuantity(@PathVariable Long productId, @RequestParam Integer quantity) {
        cartService.updateProductQuantity(productId, quantity,"decrease");
        return "redirect:/carts/my-cart";
    }

    @GetMapping("/{productId}/delete")
    public String deleteProductFromCart(@PathVariable Long productId) {
        cartService.deleteProductFromCart(productId);
        return "redirect:/carts/my-cart";
    }

}
