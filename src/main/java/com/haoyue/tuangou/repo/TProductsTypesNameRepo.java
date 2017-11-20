package com.haoyue.tuangou.repo;

import com.haoyue.repo.BaseRepo;
import com.haoyue.tuangou.pojo.TProductsTypesName;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by LiJia on 2017/11/8.
 */
public interface TProductsTypesNameRepo extends BaseRepo<TProductsTypesName,Integer> {
    @Query(nativeQuery = true,value = "select * from t_product_type_name where sale_id=?1")
    TProductsTypesName findBySaleId(String saleId);
}
