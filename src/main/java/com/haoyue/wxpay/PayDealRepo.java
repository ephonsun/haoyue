package com.haoyue.wxpay;

import com.haoyue.repo.BaseRepo;

/**
 * Created by LiJia on 2017/9/19.
 */
public interface PayDealRepo extends BaseRepo<PayDeal,Integer>{
    PayDeal findByOut_trade_no(String out_trade_no);
}
