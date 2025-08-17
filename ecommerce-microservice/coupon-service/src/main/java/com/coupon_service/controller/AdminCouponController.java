package com.coupon_service.controller;

import com.coupon_service.clients.UserClient;
import com.coupon_service.dto.CartDTO;
import com.coupon_service.dto.UserDTO;
import com.coupon_service.entity.Coupon;
import com.coupon_service.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@AllArgsConstructor
public class AdminCouponController {
    private CouponService couponService;
    private final UserClient userClient;

    @PostMapping("/apply")
    public ResponseEntity<CartDTO> applyCoupon(
            @RequestParam String apply,
            @RequestParam String code,
            @RequestParam double orderValue,
            @RequestHeader("X-User-Email") String email
    ) throws Exception {

        UserDTO user = userClient.findUserByEmail(email);
        CartDTO cart;
        if(apply.equals("true"))
            cart = couponService.applyCoupon(email, code, orderValue, user);
        else cart = couponService.removeCoupon(email, code, user);

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/create")
    public ResponseEntity<Coupon> createCoupon(
            @RequestBody Coupon coupon
    ){
        return new ResponseEntity<>(couponService.createCoupon(coupon), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id, @RequestHeader("X-User-Email") String email) throws Exception {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Coupon>> getAllCoupons(@RequestHeader("X-User-Email") String email){
        return new ResponseEntity<>(couponService.findAllCoupons(), HttpStatus.OK);
    }


}