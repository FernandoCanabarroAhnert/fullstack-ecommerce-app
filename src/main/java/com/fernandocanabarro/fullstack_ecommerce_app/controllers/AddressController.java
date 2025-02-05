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

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.AddressDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AddressService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/create")
    public String createAddressForm(Model model) {
        AddressDTO addressDto = new AddressDTO();
        model.addAttribute("addressDto", addressDto);
        return "address/create-address-form";
    }

    @PostMapping
    public String createAddress(@Valid @ModelAttribute("addressDto") AddressDTO addressDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("addressDto", addressDto);
            return "address/create-address-form";
        }
        addressService.addAddress(addressDto);
        return "redirect:/my-account";
    }

    @GetMapping("/{id}/update")
    public String updateAddressForm(@PathVariable Long id, Model model) {
        AddressDTO addressDto = addressService.findById(id);
        model.addAttribute("addressDto", addressDto);
        return "address/update-address-form";
    }

    @PostMapping("/{id}")
    public String updateAddress(@Valid @ModelAttribute("addressDto") AddressDTO addressDto, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("addressDto", addressDto);
            return "address/update-address-form";
        }
        addressService.updateAddress(id,addressDto);
        return "redirect:/my-account";
    }

    @GetMapping("/{id}/delete")
    public String deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return "redirect:/my-account";
    }
}
