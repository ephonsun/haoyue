package com.haoyue.repo;

import com.haoyue.pojo.CustomProductsTypes;

import java.util.List;

/**
 * Created by LiJia on 2017/12/8.
 */
public interface CustomProductsTypesRepo extends BaseRepo<CustomProductsTypes,Integer> {
    List<CustomProductsTypes> findBySellerId(String sellerId);
}
