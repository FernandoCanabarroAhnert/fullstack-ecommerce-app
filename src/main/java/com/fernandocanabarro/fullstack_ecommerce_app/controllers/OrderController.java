package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.AddressDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CreditCardResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductOrderItemResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserDiscountCouponResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AddressService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CreditCardService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.DiscountCouponService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.OrderService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.impl.JasperService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private JasperService jasperService;
    @Autowired
    private DiscountCouponService discountCouponService;
    @Autowired
    private AuthService authService;

    @GetMapping("/create-from-cart")
    public String createOrderRequestFromCartForm(Model model) {
        OrderRequestDTO orderRequestDto = new OrderRequestDTO();
        List<ProductOrderItemResponseDTO> orderItems = orderService.convertCartItemsToOrderItemResponseDTO();
        List<AddressDTO> addresses = addressService.findConnectedUserAddresses();
        List<CreditCardResponseDTO> creditCards = creditCardService.getConnectedUserCreditCards();
        String totalOrderValue = String.valueOf(orderItems.stream().map(orderItem -> BigDecimal.valueOf(Double.parseDouble(orderItem.getSubTotal().replace(",", "."))))
            .reduce(BigDecimal.valueOf(0.0), (x,y) -> x.add(y))
            .setScale(2, RoundingMode.HALF_UP))
            .replace(".", ",");
        List<String> installments = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            double value = Double.parseDouble(totalOrderValue.replace(",", ".")) / i;
            String insttalmentPrice = String.format("%.2f", value);
            installments.add(insttalmentPrice);
        };
        List<UserDiscountCouponResponseDTO> coupons = discountCouponService.getConnectedUserAvailableDiscountCoupons();
        int quantity = orderItems.stream().mapToInt(ProductOrderItemResponseDTO::getQuantity).sum();

        model.addAttribute("orderRequestDto", orderRequestDto);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("addresses", addresses);
        model.addAttribute("creditCards", creditCards);
        model.addAttribute("installments", installments);
        model.addAttribute("totalOrderValue", totalOrderValue);
        model.addAttribute("coupons", coupons);
        model.addAttribute("orderQuantity", quantity);

        return "order/create-order-from-cart-form";
    }

    @PostMapping("/from-cart")
    public String createOrderFromCart(@Valid @ModelAttribute("orderRequestDto") OrderRequestDTO orderRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<ProductOrderItemResponseDTO> orderItems = orderService.convertCartItemsToOrderItemResponseDTO();
            List<AddressDTO> addresses = addressService.findConnectedUserAddresses();
            List<CreditCardResponseDTO> creditCards = creditCardService.getConnectedUserCreditCards();
            String totalOrderValue = String.valueOf(orderItems.stream().map(orderItem -> BigDecimal.valueOf(Double.parseDouble(orderItem.getSubTotal().replace(",", "."))))
                .reduce(BigDecimal.valueOf(0.0), (x,y) -> x.add(y))
                .setScale(2, RoundingMode.HALF_UP))
                .replace(".", ",");
            List<String> installments = new ArrayList<>();
            for (int i = 1; i <= 6; i++) {
                double value = Double.parseDouble(totalOrderValue.replace(",", ".")) / i;
                String insttalmentPrice = String.format("%.2f", value);
                installments.add(insttalmentPrice);
            };
            List<UserDiscountCouponResponseDTO> coupons = discountCouponService.getConnectedUserAvailableDiscountCoupons();
            int quantity = orderItems.stream().mapToInt(ProductOrderItemResponseDTO::getQuantity).sum();

            model.addAttribute("orderRequestDto", orderRequestDto);
            model.addAttribute("orderItems", orderItems);
            model.addAttribute("addresses", addresses);
            model.addAttribute("creditCards", creditCards);
            model.addAttribute("installments", installments);
            model.addAttribute("totalOrderValue", totalOrderValue);
            model.addAttribute("coupons", coupons);
            model.addAttribute("orderQuantity", quantity);

            return "order/create-order-from-cart-form";
        }
        orderService.createOrderFromCart(orderRequestDto);
        return "redirect:/my-account";
    }

    @GetMapping("/create")
    public String createOrderRequestFromProductRequestForm(@RequestParam(name = "productId",required = true) Long productId,
        @RequestParam(name = "quantity", required = true) Integer quantity, Model model) {
        OrderRequestDTO orderRequestDto = new OrderRequestDTO();
        ProductOrderItemResponseDTO orderItem = orderService.getOrderItemResponseDTOFromRequest(productId, quantity);
        List<AddressDTO> addresses = addressService.findConnectedUserAddresses();
        List<CreditCardResponseDTO> creditCards = creditCardService.getConnectedUserCreditCards();
        String totalOrderValue = orderItem.getSubTotal();
        List<String> installments = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            double value = Double.parseDouble(totalOrderValue.replace(",", ".")) / i;
            String insttalmentPrice = String.format("%.2f", value);
            installments.add(insttalmentPrice);
        };
        List<UserDiscountCouponResponseDTO> coupons = discountCouponService.getConnectedUserAvailableDiscountCoupons();

        model.addAttribute("orderRequestDto", orderRequestDto);
        model.addAttribute("orderItem", orderItem);
        model.addAttribute("addresses", addresses);
        model.addAttribute("creditCards", creditCards);
        model.addAttribute("installments", installments);
        model.addAttribute("totalOrderValue", totalOrderValue);
        model.addAttribute("productId", productId);
        model.addAttribute("coupons", coupons);
        model.addAttribute("orderQuantity", quantity);

        return "order/create-order-from-product-request-form";
    }

    @PostMapping("/from-product-request")
    public String createOrderRequestFromProductRequest(@Valid @ModelAttribute("orderRequestDto") OrderRequestDTO orderRequestDto, BindingResult bindingResult, Model model,
        @RequestParam(name = "productId",required = true) Long productId, @RequestParam(name = "quantity", required = true) Integer quantity) {
        if (bindingResult.hasErrors()) {
            ProductOrderItemResponseDTO orderItem = orderService.getOrderItemResponseDTOFromRequest(productId, quantity);
            List<AddressDTO> addresses = addressService.findConnectedUserAddresses();
            List<CreditCardResponseDTO> creditCards = creditCardService.getConnectedUserCreditCards();
            String totalOrderValue = orderItem.getSubTotal();
            List<String> installments = new ArrayList<>();
            for (int i = 1; i <= 6; i++) {
                double value = Double.parseDouble(totalOrderValue.replace(",", ".")) / i;
                String insttalmentPrice = String.format("%.2f", value);
                installments.add(insttalmentPrice);
            };
            List<UserDiscountCouponResponseDTO> coupons = discountCouponService.getConnectedUserAvailableDiscountCoupons();

            model.addAttribute("orderRequestDto", orderRequestDto);
            model.addAttribute("orderItem", orderItem);
            model.addAttribute("addresses", addresses);
            model.addAttribute("creditCards", creditCards);
            model.addAttribute("installments", installments);
            model.addAttribute("totalOrderValue", totalOrderValue);
            model.addAttribute("productId", productId);
            model.addAttribute("coupons", coupons);
            model.addAttribute("orderQuantity", quantity);

            return "order/create-order-from-product-request-form";
        }
        orderService.createOrderFromProductRequest(productId, quantity, orderRequestDto);
        return "redirect:/my-account";
    }

    @GetMapping("/order-summary/{id}/pdf")
    public void exportOrderSummaryPdf(@PathVariable Long id, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        jasperService.addParams("ORDER_ID", id);
        String fileName = "relatorio-" + id + ".pdf";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        jasperService.exportToPdf(response,JasperService.ORDER_SUMMARY);
    }

    @GetMapping("/generate-boleto")
    public void exportToPdf(HttpServletResponse response,
                            @RequestParam String totalOrderValue) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "boleto_" + currentDateTime + ".pdf";
        String headerValue = "inline; filename=" + fileName;
        response.setHeader(headerKey, headerValue);

        UserResponseDTO user = authService.getConnectedUserDTO();
        jasperService.addParams("TOTAL_ORDER_VALUE", totalOrderValue);
        jasperService.addParams("USER_ID", user.getId());
        jasperService.exportToPdf(response, JasperService.BOLETO);
    }
}
