package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TCoupon;
import com.haoyue.tuangou.service.TCouponService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/12/7.
 */

@RestController
@RequestMapping("/tuan/coupon")
public class TCouponController {

    @Autowired
    private TCouponService couponService;

    //  /tuan/coupon/list?saleId=12&openId=123
    @RequestMapping("/list")
    public TResult list(String saleId, String openId){
        List<TCoupon> coupons= couponService.findByOpenIdAndSaleId(openId,saleId);
        return new TResult(false, TGlobal.do_success,coupons);
    }


    //  /tuan/coupon/del?openId=123&couponId=优惠券ID
    @RequestMapping("/del")
    public TResult del(String openId,String couponId){
        TCoupon coupon= couponService.findOne(Integer.parseInt(couponId));
        if (!coupon.getOpenId().equals(openId)){
            return new TResult(true, TGlobal.have_no_right,null);
        }
        couponService.del(coupon);
        return new TResult(false, TGlobal.do_success,null);
    }


    // 优惠券到期 2 天内提醒
    //  /tuan/coupon/remind?openId=123&saleId=1221
    @RequestMapping("/remind")
    public TResult remind(String saleId, String openId){
        List<TCoupon> coupons= couponService.findByOpenIdAndSaleId(openId,saleId);
        List<TCoupon> result=new ArrayList<>();
        Date date=new Date();
        for (TCoupon coupon:coupons){
            if (coupon.getActive()==false||coupon.getIsuse()==true){
                continue;
            }
            if (coupon.getEndDate().before(date)){
                continue;
            }
            if (coupon.getEndDate().getTime()-date.getTime()<1000*3600*48){
                result.add(coupon);
            }
        }
        return new TResult(false, TGlobal.do_success,result);
    }

}
