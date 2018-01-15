package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TCoupon;
import com.haoyue.tuangou.repo.TCouponRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/12/7.
 */
@Service
public class TCouponService {

    @Autowired
    private TCouponRepo couponRepo;

    public List<TCoupon> findByOpenIdAndSaleId(String openId, String saleId) {
        return couponRepo.findByOpenIdAndSaleId(openId,saleId);
    }

    public TCoupon findOne(int id) {
        return couponRepo.findOne(id);
    }

    public void del(TCoupon coupon) {
        couponRepo.delete(coupon);
    }

    public void save(TCoupon coupon) {
        couponRepo.save(coupon);
    }

    public void flush() {
        Date date=new Date();
        couponRepo.flushByDate(date);
    }
}
