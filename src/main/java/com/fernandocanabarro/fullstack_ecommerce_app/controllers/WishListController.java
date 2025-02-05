package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductWishListItemDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.WishListService;

@Controller
@RequestMapping("/wishlists")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @GetMapping("/my-wishlist")
    public String getConnectedUserWishList(Model model) {
        List<ProductWishListItemDTO> wishListItems = wishListService.getConnectedUserWishList();
        model.addAttribute("wishListItems", wishListItems);
        return "site/wishlist";
    }

    @GetMapping("/{productId}/add")
    public String addProductToWishList(@PathVariable Long productId,RedirectAttributes redirectAttributes,
        @RequestHeader(value = "Referer", required = false) String referer, Model model) {
        wishListService.addProductToWishList(productId);
        redirectAttributes.addFlashAttribute("productHasBeenAddedToWishList", true);
        return referer != null ? "redirect:" + referer : "redirect:/products-grid";
    }

    @GetMapping("/{productId}/delete")
    public String removeProductFromWishList(@PathVariable Long productId) {
        wishListService.removeProductFromWishList(productId);
        return "redirect:/wishlists/my-wishlist";
    }
}
