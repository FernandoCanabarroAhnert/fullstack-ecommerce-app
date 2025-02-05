package com.fernandocanabarro.fullstack_ecommerce_app.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.UserDiscountCoupon;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.pk.UserDiscountCouponPK;

@Repository
public interface UserDiscountCouponRepository extends JpaRepository<UserDiscountCoupon, UserDiscountCouponPK> {

    @Query("""
        SELECT obj FROM UserDiscountCoupon obj
        WHERE obj.id.user.id = :userId
        AND obj.isUsed = false
        AND obj.expiresAt > :moment   
        """)
    List<UserDiscountCoupon> findUsersAvailableDiscountCoupons(Long userId,LocalDateTime moment);
}
