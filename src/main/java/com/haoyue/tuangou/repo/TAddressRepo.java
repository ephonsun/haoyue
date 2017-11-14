package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TAddress;

/**
 * Created by LiJia on 2017/11/13.
 */
public interface TAddressRepo extends TBaseRepo<TAddress,Integer> {
    TAddress findByOpenIdAndSaleId(String openId, String saleId);
}
