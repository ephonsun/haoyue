package com.haoyue.repo;

import com.haoyue.pojo.AfterSale;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LiJia on 2017/9/20.
 */
public interface AfterSaleRepo extends BaseRepo<AfterSale,Integer> {

    @Query(nativeQuery = true,value = "select * from after_sale where order_id=?1")
    List<AfterSale> findByOrderId(int oid);

    List<AfterSale> findByIsAgree(String s);

    @Query(nativeQuery = true,value = "select * from after_sale where pages=?1 and closed=false")
    List<AfterSale> findByPages(String s);
}
