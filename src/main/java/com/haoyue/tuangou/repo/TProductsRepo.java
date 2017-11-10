package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TProducts;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LiJia on 2017/11/7.
 */
public interface TProductsRepo extends TBaseRepo<TProducts,Integer> {

    @Query(nativeQuery = true,value = "select * from t_products where is_tuan=true and active=true and sale_id=?1")
    List<TProducts> findByTuanProduct(String saleId);
}
