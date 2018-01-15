package com.haoyue.tuangou.repo;

import com.haoyue.repo.BaseRepo;
import com.haoyue.tuangou.pojo.TProductsTypes;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by LiJia on 2017/11/8.
 */
public interface TProductsTypesRepo extends BaseRepo<TProductsTypes,Integer> {

    @Query(nativeQuery = true,value = "select tproducts_id from t_products_products_types where products_types_id=?1")
    int findPidByPtypeId(Integer id);
}
