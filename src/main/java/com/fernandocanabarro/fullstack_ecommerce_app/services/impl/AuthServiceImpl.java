package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ActivateAccountDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.EmailDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.PasswordRecoverDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.RegistrationRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.RoleDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserNewPasswordDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserUpdateDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserUpdateInfoDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.ActivationCode;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Cart;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.DiscountCoupon;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.PasswordRecover;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Role;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.UserDiscountCoupon;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.WishList;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.pk.UserDiscountCouponPK;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ActivationCodeRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.CartRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.DiscountCouponRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.PasswordRecoverRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.RoleRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.UserDiscountCouponRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.UserRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.WishListRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.EmailService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;
import com.sendgrid.helpers.mail.Mail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final WishListRepository wishListRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final PasswordRecoverRepository passwordRecoverRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final DiscountCouponRepository discountCouponRepository;
    private final UserDiscountCouponRepository userDiscountCouponRepository;

    @Override
    @Transactional
    public void register(RegistrationRequestDTO dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        user.setBirthDate(dto.getBirthDate());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActivated(false);
        user.addRole(roleRepository.findByAuthority("ROLE_USER"));

        user.setAddresses(new ArrayList<>(Arrays.asList()));
        user.setOrders(new ArrayList<>(Arrays.asList()));
        user.setCreditCards(new ArrayList<>(Arrays.asList()));

        userRepository.save(user);

        DiscountCoupon discountCoupon = discountCouponRepository.findByCode("PRIMEIRACOMPRA20%").get();
        UserDiscountCouponPK pk = new UserDiscountCouponPK(discountCoupon, user);
        UserDiscountCoupon userDiscountCoupon = new UserDiscountCoupon(pk, LocalDateTime.now().plusDays(discountCoupon.getAvailableDays()), false, null);
        userDiscountCouponRepository.save(userDiscountCoupon);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>(Arrays.asList()));
        cartRepository.save(cart);

        WishList wishList = new WishList();
        wishList.setUser(user);
        wishList.setItems(new HashSet<>(Arrays.asList()));
        wishListRepository.save(wishList);

        user.setCart(cart);
        user.setWishList(wishList);

        userRepository.save(user);

        sendConfirmationEmail(user);
    }

    @Override
    @Transactional
    public void sendConfirmationEmail(User user) {
        String code = createActivationCodeAndSave(user);
        Mail mail = emailService.createConfirmationEmailTemplate(user.getFullName(), user.getEmail(), code);
        emailService.sendEmail(mail);
    }

    @Override
    @Transactional
    public String createActivationCodeAndSave(User user) {
        String code = createCode();
        ActivationCode activationCode = new ActivationCode(code, user);
        activationCodeRepository.save(activationCode);
        return code;
    }

    @Override
    @Transactional
    public String createCode() {
        String chars = "0123456789";
        int size = chars.length();
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int randomIndex = secureRandom.nextInt(size);
            builder.append(chars.charAt(randomIndex));
        }
        return builder.toString();
    }

    @Override
    @Transactional
    public void activateAccount(ActivateAccountDTO dto) {
        Optional<ActivationCode> activationCode = activationCodeRepository.findByCode(dto.getCode());
        activationCode.get().setValidatedAt(LocalDateTime.now());
        User user = userRepository.findByEmail(activationCode.get().getUser().getEmail()).get();
        user.setActivated(true);
        userRepository.save(user);
        activationCodeRepository.save(activationCode.get());
    }

    @Override
    @Transactional
    public void createPasswordRecover(EmailDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail()).get();
        String code = createCode();
        PasswordRecover passwordRecover = new PasswordRecover(code,user);
        passwordRecoverRepository.save(passwordRecover);
        Mail mail = emailService.createPasswordRecoverTemplate(user.getFullName(), user.getEmail(), code);
        emailService.sendEmail(mail);
    }

    @Override
    @Transactional
    public void saveNewPassword(PasswordRecoverDTO dto) {
        PasswordRecover passwordRecover = passwordRecoverRepository.findByCode(dto.getCode()).get();
        User user = passwordRecover.getUser();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        passwordRecover.setValidatedAt(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> adminFindAll() {
        return userRepository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> adminFindAllUsersPaginated(String name,Pageable pageable) {
        return userRepository.findByFullNameContainingIgnoreCase(name,pageable)
            .map(UserResponseDTO::new);
    }

    @Override
    @Transactional
    public void adminCreateUser(UserRequestDTO dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        user.setBirthDate(dto.getBirthDate());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActivated(dto.getActivated());

        dto.getRoles().forEach(roleId -> {
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Função não encontrada"));
            user.addRole(role);
        });

        user.setAddresses(new ArrayList<>(Arrays.asList()));
        user.setOrders(new ArrayList<>(Arrays.asList()));
        user.setCreditCards(new ArrayList<>(Arrays.asList()));

        userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>(Arrays.asList()));
        cartRepository.save(cart);

        WishList wishList = new WishList();
        wishList.setUser(user);
        wishList.setItems(new HashSet<>(Arrays.asList()));
        wishListRepository.save(wishList);

        user.setCart(cart);
        user.setWishList(wishList);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void adminUpdateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        if (dto.getBirthDate() != null) {
            user.setBirthDate(dto.getBirthDate());
        }
        user.setActivated(dto.getActivated());
        user.getRoles().clear();
        dto.getRoles().forEach(roleId -> {
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Função não encontrada"));
            user.addRole(role);
        });
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAllRoles() {
        return roleRepository.findAll().stream()
            .map(RoleDTO::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserUpdateDTO findByIdToUpdate(Long id) {
        return userRepository.findById(id)
            .map(UserUpdateDTO::new)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    @Override
    @Transactional
    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getConnectedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
            .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getConnectedUserDTO() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
            .map(UserResponseDTO::new)
            .orElse(null);
    }

    @Override
    @Transactional
    public void userUpdatePassword(UserNewPasswordDTO dto) {
        User user = getConnectedUser();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void userUpdateInfo(UserUpdateInfoDTO dto) {
        User user = getConnectedUser();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        if (dto.getBirthDate() != null) {
            user.setBirthDate(dto.getBirthDate());
        }
        userRepository.save(user);
    }
}
