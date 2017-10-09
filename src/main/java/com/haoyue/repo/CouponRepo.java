package com.haoyue.repo;

import com.haoyue.pojo.Coupon;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LiJia on 2017/9/28.
 */
public interface CouponRepo extends BaseRepo<Coupon,Integer> {

    Coupon findByCodeAndSellerId(String code, String sellerId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from coupons where seller_id =?1")
    void deleteBySellerId(String sellerId);
}
