package com.haoyue.repo;

import com.haoyue.pojo.Collection;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 */
public interface CollectionsRepo extends BaseRepo<Collection,Integer> {
    List<Collection> findByOpenIdAndSellerId(String openId, String sellerId);

    List<Collection> findBySellerIdAndCreateDate(String sellerId, Date date);
}
