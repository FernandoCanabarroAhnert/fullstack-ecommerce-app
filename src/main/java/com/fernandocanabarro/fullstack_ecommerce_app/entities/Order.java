package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.enums.OrderStatus;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@EqualsAndHashCode(of = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_fk", value = ConstraintMode.CONSTRAINT), nullable = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime moment;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "address_id", foreignKey = @ForeignKey(name = "address_fk", value = ConstraintMode.CONSTRAINT))
    private Address address;

    @ManyToOne
    @JoinColumn(name = "discount_coupon_id", foreignKey = @ForeignKey(name = "discount_coupon_fk", value = ConstraintMode.CONSTRAINT))
    private DiscountCoupon discountCoupon;

    @OneToMany(mappedBy = "id.order")
    private List<ProductOrderItem> items = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(name = "payment_fk", value = ConstraintMode.CONSTRAINT))
    private Payment payment;

    public BigDecimal getTotalValue() {
        BigDecimal sum = BigDecimal.ZERO;
        for (ProductOrderItem item : items) {
            sum = sum.add(item.getTotalItemPrice());
        }
        return sum;
    }
}
