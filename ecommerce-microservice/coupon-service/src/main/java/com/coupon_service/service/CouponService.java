package com.coupon_service.service;

import com.coupon_service.dto.CartDTO;
import com.coupon_service.dto.UserDTO;
import com.coupon_service.entity.Coupon;

import java.util.List;

public interface CouponService {

    CartDTO applyCoupon(String jwt, String code, double orderValue, UserDTO user) throws Exception;
    CartDTO removeCoupon(String jwt, String code, UserDTO user) throws Exception;
    Coupon findCouponById(Long id) throws Exception;
    Coupon createCoupon(Coupon coupon);
    List<Coupon> findAllCoupons();
    void deleteCoupon(Long id) throws Exception;
}