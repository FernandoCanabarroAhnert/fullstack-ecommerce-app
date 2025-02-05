package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.UserDiscountCoupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDiscountCouponResponseDTO {

    private Long discountCouponId;
    private String description;
    private String code;
    private BigDecimal discountPercentage;
    private String expiresAt;

    public UserDiscountCouponResponseDTO(UserDiscountCoupon entity) {
        discountCouponId = entity.getId().getDiscountCoupon().getId();
        description = entity.getId().getDiscountCoupon().getDescription();
        code = entity.getId().getDiscountCoupon().getCode();
        discountPercentage = entity.getId().getDiscountCoupon().getDiscountPercentage();
        expiresAt = entity.getExpiresAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
