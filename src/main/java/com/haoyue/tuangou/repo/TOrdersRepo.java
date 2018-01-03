package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TOrders;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by LiJia on 2017/11/9.
 */
public interface TOrdersRepo extends TBaseRepo<TOrders,Integer> {
    TOrders findByCode(String code);

    List<TOrders> findByState(String order_unreceive);

    List<TOrders> findBySaleId(String id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_orders set wxname=?2 where open_id=?1")
    void updateWxname(String openId, String wxname);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_orders set wxpic=?2 where open_id=?1")
    void updateWxpic(String openId, String wxpic);
}
