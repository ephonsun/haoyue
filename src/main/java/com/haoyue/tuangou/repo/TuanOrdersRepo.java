package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TuanOrders;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/9.
 */
public interface TuanOrdersRepo extends TBaseRepo<TuanOrders,Integer> {
    List<TuanOrders> findByGroupCode(String groupcode);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_tuanorders set isover=true where end_date < ?1 and isover=false ")
    void flushdata(Date date);

    @Query(nativeQuery = true,value = "select * from t_tuanorders where out_trade_no=?1 ")
    TuanOrders findByOut_trade_no(String out_trade_no);

    @Query(nativeQuery = true,value = "select open_id from t_tuanorders where group_code=?1 ")
    List<String> findOpenIdsByGroupCode(String groupcode);

    TuanOrders findByCode(String ordercode);

    List<TuanOrders> findByState(String tuan_order_unreive);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_tuanorders set isover=true  where t_products_id=?1 and state='正在拼团团购订单' ")
    void updateEndByPid(Integer id);

    @Query(nativeQuery = true,value = "select * from t_tuanorders where t_products_id=?1 and state='正在拼团团购订单'")
    List<TuanOrders>  findTuaningByPid(Integer id);

    @Query(nativeQuery = true,value = "select * from t_tuanorders where isover=true and state='正在拼团团购订单' and ispayback=false")
    List<TuanOrders>  findUnPayback();

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_tuanorders set ispayback=true  where id=?1")
    void updatePayback(Integer id);
}
