package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discount_coupons",indexes = {
    @Index(name = "code_idx",columnList = "code")
})
public class DiscountCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String code;

    private BigDecimal discountPercentage;

    private Integer availableDays;

    @OneToMany(mappedBy = "id.discountCoupon",fetch = FetchType.LAZY)
    private List<UserDiscountCoupon> users = new ArrayList<>();
}
