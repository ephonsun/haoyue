package com.haoyue.service;

import com.haoyue.pojo.Coupon;
import com.haoyue.repo.CouponRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LiJia on 2017/9/28.
 */
@Service
public class CouponService {

    @Autowired
    private CouponRepo couponRepo;

    public List<Coupon> save(List<Coupon> coupons) {
        return  couponRepo.save(coupons);
    }

    public Coupon findByCodeAndSellerId(String code, String sellerId) {
        return couponRepo.findByCodeAndSellerId(code,sellerId);
    }

    public void update(Coupon coupon) {
        couponRepo.save(coupon);
    }

    public void deleteBySellerId(String sellerId) {
        couponRepo.deleteBySellerId(sellerId);
    }
}
