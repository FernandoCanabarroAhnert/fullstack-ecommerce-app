package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.DiscountCouponCodeDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.DiscountCouponDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserDiscountCouponResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.DiscountCoupon;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.UserDiscountCoupon;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.DiscountCouponRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.UserDiscountCouponRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.DiscountCouponService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscountCouponServiceImpl implements DiscountCouponService {

    private final DiscountCouponRepository discountCouponRepository;
    private final UserDiscountCouponRepository userDiscountCouponRepository;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public List<DiscountCouponDTO> adminFindAll() {
        return discountCouponRepository.findAll().stream()
            .map(DiscountCouponDTO::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiscountCouponDTO> adminFindAllPaginated(String description,Pageable pageable) {
        return discountCouponRepository.findByDescriptionContainingIgnoreCase(description, pageable)
            .map(DiscountCouponDTO::new);
    }

    @Override
    @Transactional
    public void adminCreateDiscountCoupon(DiscountCouponDTO dto) {
        DiscountCoupon coupon = new DiscountCoupon();
        coupon.setDescription(dto.getDescription());
        coupon.setCode(dto.getCode());
        coupon.setDiscountPercentage(dto.getDiscountPercentage());
        coupon.setAvailableDays(dto.getAvailableDays());
        discountCouponRepository.save(coupon);
    }

    @Override
    @Transactional
    public void adminUpdateDiscountCoupon(Long id, DiscountCouponDTO dto) {
        DiscountCoupon coupon = discountCouponRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
        coupon.setDescription(dto.getDescription());
        coupon.setCode(dto.getCode());
        coupon.setDiscountPercentage(dto.getDiscountPercentage());
        coupon.setAvailableDays(dto.getAvailableDays());
        discountCouponRepository.save(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountCouponDTO adminFindByIdToUpdate(Long id) {
        return discountCouponRepository.findById(id)
            .map(DiscountCouponDTO::new)
            .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
    }

    @Override
    @Transactional
    public void addDiscountCouponToUser(DiscountCouponCodeDTO dto) {
        DiscountCoupon coupon = discountCouponRepository.findByCode(dto.getCode())
            .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
        User user = authService.getConnectedUser();
        UserDiscountCoupon userDiscountCoupon = new UserDiscountCoupon(coupon, user,
            LocalDateTime.now().plusDays(coupon.getAvailableDays()), false, null);
        userDiscountCouponRepository.save(userDiscountCoupon);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDiscountCouponResponseDTO> getConnectedUserAvailableDiscountCoupons() {
        return userDiscountCouponRepository.findUsersAvailableDiscountCoupons(authService.getConnectedUser().getId(), LocalDateTime.now())
            .stream().map(UserDiscountCouponResponseDTO::new).toList();
    }

}
