package com.fernandocanabarro.fullstack_ecommerce_app.entities.pk;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.DiscountCoupon;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserDiscountCouponPK {

    @ManyToOne
    @JoinColumn(name = "discount_coupon_id",foreignKey = @ForeignKey(name = "discount_coupon_fk",value = ConstraintMode.CONSTRAINT))
    private DiscountCoupon discountCoupon;
    @ManyToOne
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "user_fk",value = ConstraintMode.CONSTRAINT))
    private User user;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((discountCoupon == null) ? 0 : discountCoupon.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        UserDiscountCouponPK other = (UserDiscountCouponPK) obj;
        if (discountCoupon == null) {
            if (other.discountCoupon != null)
                return false;
        } else if (!discountCoupon.equals(other.discountCoupon))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    
}
