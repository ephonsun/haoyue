package com.haoyue.tuangou.wxpay;

import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by LiJia on 2017/9/19.
 */
public interface TPayDealRepo extends BaseRepo<TPayDeal,Integer>{


    @Query(nativeQuery =true,value = "select * from t_paydeals where out_trade_no=?1")
    TPayDeal findByOut_trade_no(String out_trade_no);
}
