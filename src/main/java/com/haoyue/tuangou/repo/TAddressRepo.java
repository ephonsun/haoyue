package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TAddress;

import java.util.List;

/**
 * Created by LiJia on 2017/11/13.
 */
public interface TAddressRepo extends TBaseRepo<TAddress,Integer> {
    List<TAddress> findByOpenIdAndSaleId(String openId, String saleId);
}
