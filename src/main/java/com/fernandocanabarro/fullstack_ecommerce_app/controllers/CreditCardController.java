package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CreditCardRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CreditCardService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/credit-cards")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @GetMapping("/create")
    public String createCreditCardForm(Model model) {
        CreditCardRequestDTO creditCardDto = new CreditCardRequestDTO();
        model.addAttribute("creditCardDto", creditCardDto);
        return "credit-card/create-credit-card-form";
    }

    @PostMapping
    public String createCreditCard(@Valid @ModelAttribute("creditCardDto") CreditCardRequestDTO creditCardDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("creditCardDto", creditCardDto);
            return "credit-card/create-credit-card-form";
        }
        creditCardService.createCreditCard(creditCardDto);
        return "redirect:/my-account";
    }

    @GetMapping("/{id}/delete")
    public String deleteCreditCard(@PathVariable Long id) {
        creditCardService.deleteCreditCard(id);
        return "redirect:/my-account";
    }
}
