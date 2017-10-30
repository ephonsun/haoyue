package com.haoyue.repo;

import com.haoyue.pojo.Order;
import com.haoyue.repo.BaseRepo;

import java.util.List;


/**
 * Created by LiJia on 2017/8/24.
 */
public interface OrderRepo extends BaseRepo<Order,Integer> {

    List<Order> findByState(String order_send);
}
