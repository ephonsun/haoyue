package com.haoyue.wxpay;

import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by LiJia on 2017/9/19.
 */
public interface PayDealRepo extends BaseRepo<PayDeal,Integer>{

    @Query(nativeQuery = true,value = "select * from paydeals where out_trade_no=?1 ")
    PayDeal findByOut_trade_no(String out_trade_no);
}
