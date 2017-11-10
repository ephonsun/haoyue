package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TuanOrders;

import java.util.List;

/**
 * Created by LiJia on 2017/11/9.
 */
public interface TuanOrdersRepo extends TBaseRepo<TuanOrders,Integer> {
    List<TuanOrders> findByGroupCode(String groupcode);
}
