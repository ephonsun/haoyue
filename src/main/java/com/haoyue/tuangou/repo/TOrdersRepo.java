package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TOrders;

/**
 * Created by LiJia on 2017/11/9.
 */
public interface TOrdersRepo extends TBaseRepo<TOrders,Integer> {
    TOrders findByCode(String code);
}
