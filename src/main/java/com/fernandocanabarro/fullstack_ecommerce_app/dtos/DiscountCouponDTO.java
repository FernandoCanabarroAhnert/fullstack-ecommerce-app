package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.math.BigDecimal;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.DiscountCoupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountCouponDTO {

    private Long id;
    @NotBlank(message = "Campo Requerido")
    private String description;
    @NotBlank(message = "Campo Requerido")
    private String code;
    @NotNull(message = "Campo Requerido")
    private BigDecimal discountPercentage;
    @NotNull(message = "Campo Requerido")
    @Min(1)
    private Integer availableDays;

    public DiscountCouponDTO(DiscountCoupon entity) {
        id = entity.getId();
        description = entity.getDescription();
        code = entity.getCode();
        discountPercentage = entity.getDiscountPercentage();
        availableDays = entity.getAvailableDays();
    }
}
