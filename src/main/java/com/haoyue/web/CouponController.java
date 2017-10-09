package com.haoyue.web;

import com.haoyue.pojo.Coupon;
import com.haoyue.service.CouponService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/9/28.
 * 优惠券
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @RequestMapping("/getAuto")
    public Result getCouponAuto(Integer amount,String sellerId){

        if (amount==null||amount==0){
            return new Result(true,Global.data_unright,null,null);
        }
        Date date=new Date();
        List<Coupon> coupons=new ArrayList<>();
        for(int i=0;i<amount;i++){
            Coupon coupon=new Coupon();
            coupon.setCreateDate(date);
            coupon.setSellerId(sellerId);
            //优惠券码随机 数字和字母
            coupon.setCode(StringUtils.checkCode(6)+Global.count++);
            if (Global.count >= 5000) {
                Global.count = 1;
            }
            coupons.add(coupon);
        }
        couponService.save(coupons);
        return new Result(false, Global.do_success,coupons,null);
    }


    @RequestMapping("/checkout")
    public Result spendCoupon(String openId,String code,String sellerId){
        Coupon coupon=couponService.findByCodeAndSellerId(code,sellerId);
        //优惠券不存在
        if (coupon==null){
            return new Result(true, Global.coupon_not_exist,null,null);
        }
        if (coupon.getOpenId()==null){
            coupon.setOpenId(openId);
            couponService.update(coupon);
        }
        //优惠券被他人使用
        else if(coupon.getOpenId()!=null&&!coupon.getOpenId().equals(openId)){
            return new Result(true, Global.coupon_isuse,null,null);
        }
        return new Result(false, Global.do_success,null,null);
    }

}
