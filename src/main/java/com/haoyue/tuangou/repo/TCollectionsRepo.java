package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TCollections;

/**
 * Created by LiJia on 2017/11/8.
 */
public interface TCollectionsRepo extends TBaseRepo<TCollections,Integer> {
    TCollections findByCidAndSaleId(String cid, String saleId);
}
