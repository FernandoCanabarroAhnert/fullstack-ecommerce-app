package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.AddressDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CreditCardResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.DiscountCouponCodeDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductGridDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserDiscountCouponResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserNewPasswordDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserUpdateInfoDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AddressService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.BrandService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CategoryService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CreditCardService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.DiscountCouponService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.OrderService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.ProductService;

import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthService authService;
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DiscountCouponService discountCouponService;
    
    @GetMapping("/")
    public String home(Model model){
        List<CategoryResponseDTO> categories = categoryService.findAll();
        List<BrandResponseDTO> brands = brandService.findAll();
        List<ProductGridDTO> products = productService.getProductsInOffer();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("products", products);
        return "site/home";
    }

    @GetMapping("/my-account")
    public String getConnectedUserAccountInfo(Model model) {
        List<AddressDTO> addresses = addressService.findConnectedUserAddresses();
        List<CreditCardResponseDTO> creditCards = creditCardService.getConnectedUserCreditCards();
        UserNewPasswordDTO newPasswordDto = new UserNewPasswordDTO();
        UserUpdateInfoDTO userUpdateInfoDto = new UserUpdateInfoDTO();
        UserResponseDTO user = authService.getConnectedUserDTO();
        List<OrderResponseDTO> orders = orderService.getConnectedUserOrders();
        List<UserDiscountCouponResponseDTO> coupons = discountCouponService.getConnectedUserAvailableDiscountCoupons();
        DiscountCouponCodeDTO couponCodeDTO = new DiscountCouponCodeDTO();
        model.addAttribute("addresses", addresses); 
        model.addAttribute("creditCards", creditCards);
        model.addAttribute("newPasswordDto", newPasswordDto);
        model.addAttribute("userUpdateInfoDto", userUpdateInfoDto);
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("coupons", coupons);
        model.addAttribute("couponCodeDTO", couponCodeDTO);
        return "site/account";
    }

    @PostMapping("/add-coupon")
    public String addDiscountCouponToUser(@Valid @ModelAttribute("couponCodeDTO") DiscountCouponCodeDTO couponCodeDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<AddressDTO> addresses = addressService.findConnectedUserAddresses();
            List<CreditCardResponseDTO> creditCards = creditCardService.getConnectedUserCreditCards();
            UserNewPasswordDTO newPasswordDto = new UserNewPasswordDTO();
            UserUpdateInfoDTO userUpdateInfoDto = new UserUpdateInfoDTO();
            UserResponseDTO user = authService.getConnectedUserDTO();
            List<OrderResponseDTO> orders = orderService.getConnectedUserOrders();
            List<UserDiscountCouponResponseDTO> coupons = discountCouponService.getConnectedUserAvailableDiscountCoupons();
            model.addAttribute("addresses", addresses); 
            model.addAttribute("creditCards", creditCards);
            model.addAttribute("newPasswordDto", newPasswordDto);
            model.addAttribute("userUpdateInfoDto", userUpdateInfoDto);
            model.addAttribute("user", user);
            model.addAttribute("orders", orders);
            model.addAttribute("coupons", coupons);
            model.addAttribute("couponCodeDTO", couponCodeDTO);
            return "site/account";
        }
        discountCouponService.addDiscountCouponToUser(couponCodeDTO);
        return "redirect:/my-account";
    }

    @GetMapping("/newsletter-subscribe")
    public String subscribeToNewsletter(@RequestParam String email) {
        if (!authService.getConnectedUserDTO().getEmail().equals(email)) {
            return "redirect:/";
        }
        DiscountCouponCodeDTO dto = new DiscountCouponCodeDTO("NEWSLETTER20%");
        discountCouponService.addDiscountCouponToUser(dto);
        return "redirect:/my-account";
    }
}
