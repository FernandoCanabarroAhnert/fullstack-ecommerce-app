package com.fernandocanabarro.fullstack_ecommerce_app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.DiscountCouponCodeDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.DiscountCouponDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserDiscountCouponResponseDTO;

public interface DiscountCouponService {

    List<DiscountCouponDTO> adminFindAll();
    Page<DiscountCouponDTO> adminFindAllPaginated(String description,Pageable pageable);
    void adminCreateDiscountCoupon(DiscountCouponDTO dto);
    void adminUpdateDiscountCoupon(Long id, DiscountCouponDTO dto);
    DiscountCouponDTO adminFindByIdToUpdate(Long id);

    void addDiscountCouponToUser(DiscountCouponCodeDTO dto);
    List<UserDiscountCouponResponseDTO> getConnectedUserAvailableDiscountCoupons();
}
