package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ActivateAccountDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.AddressDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.EmailDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.PasswordRecoverDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.RegistrationRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserNewPasswordDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserUpdateInfoDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AddressService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.OrderService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/login")
    public String loginForm(Model model){
        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        model.addAttribute("register", registrationRequestDTO);
        return "auth/login-register-form";
    }

    @PostMapping("/auth/register")
    public String register(@Valid @ModelAttribute("register") RegistrationRequestDTO register, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("register", register);
            return "auth/login-register-form";
        }
        authService.register(register);
        return "redirect:/activate-account";
    }

    @GetMapping("/activate-account")
    public String activateAccountForm(Model model){
        ActivateAccountDTO activateAccountDTO = new ActivateAccountDTO();
        model.addAttribute("activateDto", activateAccountDTO);
        return "auth/activate-account-form";
    }

    @PostMapping("/auth/activate-account")
    public String activateAccount(@Valid @ModelAttribute("activateDto") ActivateAccountDTO activateAccountDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activateDto", activateAccountDTO);
            return "auth/activate-account-form";
        }
        authService.activateAccount(activateAccountDTO);
        return "redirect:/";
    }

    @GetMapping("/password-recover")
    public String forgotPasswordForm(Model model){
        EmailDTO emailDTO = new EmailDTO();
        model.addAttribute("emailDto", emailDTO);
        return "auth/forgot-password-form";
    }

    @PostMapping("/auth/password-recover")
    public String savePasswordRecover(@Valid @ModelAttribute("emailDto") EmailDTO emailDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("emailDto", emailDTO);
            return "auth/forgot-password-form";
        }
        authService.createPasswordRecover(emailDTO);
        return "redirect:/new-password";
    }

    @GetMapping("/new-password")
    public String newPasswordForm(Model model){
        PasswordRecoverDTO passwordRecoverDTO = new PasswordRecoverDTO();
        model.addAttribute("passwordRecoverDto", passwordRecoverDTO);
        return "auth/new-password-form";
    }

    @PostMapping("/auth/new-password")
    public String saveNewPassword(@Valid @ModelAttribute("passwordRecoverDto") PasswordRecoverDTO passwordRecoverDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("passwordRecoverDto", passwordRecoverDTO);
            return "auth/new-password-form";
        }
        authService.saveNewPassword(passwordRecoverDTO);
        return "redirect:/";
    }

    @PostMapping("/auth/update-password")
    public String updatePassword(@Valid @ModelAttribute("newPasswordDto") UserNewPasswordDTO newPasswordDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<AddressDTO> addresses = addressService.findConnectedUserAddresses();
            UserResponseDTO user = authService.getConnectedUserDTO();
            UserUpdateInfoDTO userUpdateInfoDto = new UserUpdateInfoDTO();
            List<OrderResponseDTO> orders = orderService.getConnectedUserOrders();
            model.addAttribute("newPasswordDto", newPasswordDto);
            model.addAttribute("addresses", addresses);
            model.addAttribute("userUpdateInfoDto", userUpdateInfoDto);
            model.addAttribute("user", user);
            model.addAttribute("orders", orders);
            return "site/account";
        }
        authService.userUpdatePassword(newPasswordDto);
        return "redirect:/my-account";
    }

    @PostMapping("/auth/update-info")
    public String updateInfo(@Valid @ModelAttribute("userUpdateInfoDto") UserUpdateInfoDTO userUpdateInfoDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<AddressDTO> addresses = addressService.findConnectedUserAddresses();
            UserNewPasswordDTO newPasswordDto = new UserNewPasswordDTO();
            UserResponseDTO user = authService.getConnectedUserDTO();
            List<OrderResponseDTO> orders = orderService.getConnectedUserOrders();
            model.addAttribute("addresses", addresses); 
            model.addAttribute("newPasswordDto", newPasswordDto);
            model.addAttribute("userUpdateInfoDto", userUpdateInfoDto);
            model.addAttribute("user", user);
            model.addAttribute("orders", orders);
            return "site/account";
        }
        authService.userUpdateInfo(userUpdateInfoDto);
        return "redirect:/logout";
    }
}
