package com.coupon_service.service.impl;

import com.coupon_service.clients.CartClient;
import com.coupon_service.clients.UserClient;
import com.coupon_service.dto.CartDTO;
import com.coupon_service.dto.UserDTO;
import com.coupon_service.entity.Coupon;
import com.coupon_service.repo.CouponRepository;
import com.coupon_service.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CartClient cartClient;
    private final UserClient userClient;

    @Override
    public CartDTO applyCoupon(String jwt, String code, double orderValue, UserDTO user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        CartDTO cart = cartClient.findUserCart(jwt);

        if(coupon == null)
            throw new Exception("Coupon not valid");
        if(user.getUsedCoupons().contains(coupon.getId()))
            throw new Exception("Coupon already used");
        if(orderValue < coupon.getMinimumOrderValue())
            throw new Exception("valid for minimum order value -> " + coupon.getMinimumOrderValue());

        if(coupon.isActive()
                && LocalDate.now().isAfter(coupon.getValidityStartDate())
                && LocalDate.now().isBefore(coupon.getValidityEndDate())){

            user.getUsedCoupons().add(coupon.getId());
            userClient.saveUser(user);
           // userRepository.save(user);

            double discountedPrice = ( cart.getTotalSellingPrice() * coupon.getDiscountPercentage() ) / 100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(code);
            cartClient.saveCart(jwt, cart);
            return cart;
        }

        throw new Exception("Coupon not valid");
    }

    @Override
    public CartDTO removeCoupon(String jwt, String code, UserDTO user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if(coupon == null)
            throw new Exception("coupon not found...");
        CartDTO cart = cartClient.findUserCart(jwt);

        double discountedPrice = ( cart.getTotalSellingPrice() * coupon.getDiscountPercentage() )/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);
        cart.setCouponCode(null);

        return cartClient.saveCart(jwt, cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id).orElseThrow(() -> new Exception("Coupon not found"));
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public void deleteCoupon(Long id) throws Exception {
        findCouponById(id);
        couponRepository.deleteById(id);
    }
}