package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TUserBuy;

/**
 * Created by LiJia on 2017/11/8.
 */
public interface TUserBuyRepo extends TBaseRepo<TUserBuy,Integer> {
    TUserBuy findBySaleIdAndOpenId(String saleId, String openId);
}
