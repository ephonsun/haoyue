package com.haoyue.repo;

import com.haoyue.pojo.Order;
import com.haoyue.pojo.OrderTotalPrice;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * Created by LiJia on 2017/8/24.
 */
public interface OrderRepo extends BaseRepo<Order,Integer> {

    List<Order> findByState(String order_send);

    @Query(nativeQuery = true,value = "select sum(total_price) from orders where customer_id=?1 and seller_id=?2 and create_date >= ?3")
    double getToTalPriceBySellerId(String cid,String sellerId,Date date);

    @Query(nativeQuery = true,value = "select sum(total_price) as total_price , customer_id from orders where seller_id=?1 and create_date >= ?2 group by customer_id")
    List<OrderTotalPrice> getToTalPriceBySellerAndCustomer(String sellerId, Date date);

    @Query(nativeQuery = true,value ="select order_id from order_product where product_id=?1")
    List<Integer> findBySellerIdAndProIdAndIsLuckDrawEnd(Integer pid);

    @Query(nativeQuery = true,value ="select * from orders where seller_id=?1 and create_date>?2 and create_date<?3")
    List<Order> findBySellerIdAndCreateDate(String sellerId, Date from, Date end);

    @Query(nativeQuery = true,value ="select count(*) from orders where seller_id=?1 and is_luck_draw=true and state!='待付款抽奖订单'")
    int findLuckDrawSize(String sellerId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update orders set is_luck_draw_end=true where seller_id=?1 and is_luck_draw=true")
    void updateIsLuckDrawEndBySeller(String sellerId);

    @Query(nativeQuery = true,value = "select luckcode from orders where seller_id=?1 and is_luck_draw=true and is_luck_draw_end=false")
    List<String> findByLuckCodeBySeller(Integer sellerId);

    List<Order> findBySellerId(Integer sellerId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update orders set state='已完成订单' where id=?1")
    void autoDone(Integer id);

    @Query(nativeQuery = true,value = "select * from orders where customer_id=?1 and seller_id=?2  and state='已完成订单' and iscomment=false")
    List<Order> findUnComment(int cid, String sellerId);
}
