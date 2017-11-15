package com.haoyue.repo;

import com.haoyue.pojo.ProductVisitor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 */
public interface ProductVisitorRepo extends BaseRepo<ProductVisitor,Integer> {
    ProductVisitor findByOpenIdAndPidAndCreateDate(String openId, String pid, Date date);

    @Query(nativeQuery = true,value = "select * from product_visitor where seller_id=?1 and create_date=?2 group by (pid)")
    List<ProductVisitor> findBySellerIdAndCreateDate(String sellerId, Date date);
}
