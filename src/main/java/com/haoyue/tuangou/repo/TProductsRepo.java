package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TProducts;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/7.
 */
public interface TProductsRepo extends TBaseRepo<TProducts,Integer> {

    @Query(nativeQuery = true,value = "select * from t_products where is_tuan=true and active=true and sale_id=?1")
    List<TProducts> findByTuanProduct(String saleId);

    @Query(nativeQuery = true,value = "select id from t_products where  sale_id=?1 and active=true")
    List<Integer> findPidsBySaleId(String saleId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from t_products_products_types where tproducts_id=?1 ")
    void delPtypes(Integer pid);

    @Query(nativeQuery = true,value = "select * from t_products where types=?1 and sale_id=?2 and active=true ")
    List<TProducts> findByTypesAndSaleId(String typename, String saleId);

    @Query(nativeQuery = true,value = "select * from t_products where end_date<?1 and is_end=false ")
    List<TProducts> findByEndDateAndIsEnd(Date date);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_products set is_end=true where id=?1 ")
    void updateEnd(Integer id);
}
