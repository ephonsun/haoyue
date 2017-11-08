package com.haoyue.tuangou.repo;

import com.haoyue.repo.BaseRepo;
import com.haoyue.tuangou.pojo.TProductsTypesName;

/**
 * Created by LiJia on 2017/11/8.
 */
public interface TProductsTypesNameRepo extends BaseRepo<TProductsTypesName,Integer> {
    TProductsTypesName findBySaleId(String saleId);
}
