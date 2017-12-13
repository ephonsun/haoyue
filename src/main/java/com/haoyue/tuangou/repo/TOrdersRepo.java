package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TOrders;

import java.util.List;

/**
 * Created by LiJia on 2017/11/9.
 */
public interface TOrdersRepo extends TBaseRepo<TOrders,Integer> {
    TOrders findByCode(String code);

    List<TOrders> findByState(String order_unreceive);

    List<TOrders> findBySaleId(int id);

    List<TOrders> findBySaleIdAndState(int id, String state);
}
