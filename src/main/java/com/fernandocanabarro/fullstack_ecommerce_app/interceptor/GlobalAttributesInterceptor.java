package com.fernandocanabarro.fullstack_ecommerce_app.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GlobalAttributesInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    @Override
    @SuppressWarnings("null")
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            int cartItemsQuantity = authService.getConnectedUser() != null
                ? authService.getConnectedUser().getCart().getItems().size()
                : 0;
            int wishListItemsQuantity = authService.getConnectedUser() != null
                ? authService.getConnectedUser().getWishList().getItems().size()
                : 0;

            modelAndView.addObject("cartItemsQuantity", cartItemsQuantity);
            modelAndView.addObject("wishListItemsQuantity", wishListItemsQuantity);
        }
    }
}
