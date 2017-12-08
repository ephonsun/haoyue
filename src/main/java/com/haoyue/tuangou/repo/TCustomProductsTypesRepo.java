package com.haoyue.tuangou.repo;

import com.haoyue.pojo.CustomProductsTypes;
import com.haoyue.repo.BaseRepo;
import com.haoyue.tuangou.pojo.TCustomProductsTypes;

import java.util.List;

/**
 * Created by LiJia on 2017/12/8.
 */
public interface TCustomProductsTypesRepo extends BaseRepo<TCustomProductsTypes,Integer> {

    List<TCustomProductsTypes> findBySaleId(String sellerId);
}
