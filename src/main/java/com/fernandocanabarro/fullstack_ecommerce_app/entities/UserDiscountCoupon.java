package com.fernandocanabarro.fullstack_ecommerce_app.entities;

import java.time.LocalDateTime;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.pk.UserDiscountCouponPK;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
@Table(name = "user_discount_coupon")
public class UserDiscountCoupon {

    @EmbeddedId
    private UserDiscountCouponPK id = new UserDiscountCouponPK();

    private LocalDateTime expiresAt;

    private Boolean isUsed;

    private LocalDateTime usedAt;

    public UserDiscountCoupon(DiscountCoupon discountCoupon, User user, LocalDateTime expiresAt, Boolean isUsed, LocalDateTime usedAt) {
        this.id.setDiscountCoupon(discountCoupon);
        this.id.setUser(user);
        this.expiresAt = expiresAt;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDiscountCoupon other = (UserDiscountCoupon) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
