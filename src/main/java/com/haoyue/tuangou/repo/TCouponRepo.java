package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TCoupon;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/12/7.
 */
public interface TCouponRepo extends TBaseRepo<TCoupon,Integer> {
    List<TCoupon> findByOpenIdAndSaleId(String openId, String saleId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_coupon set active=false where end_date<?1 and active=true and isuse=false")
    void flushByDate(Date date);
}
