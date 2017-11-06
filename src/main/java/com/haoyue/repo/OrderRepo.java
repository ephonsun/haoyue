package com.haoyue.repo;

import com.haoyue.pojo.Order;
import com.haoyue.pojo.OrderTotalPrice;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Query;

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

    @Query(nativeQuery = true,value ="select * from order_product where product_id=?1")
    List<Integer> findBySellerIdAndProIdAndIsLuckDrawEnd(Integer pid);
}
