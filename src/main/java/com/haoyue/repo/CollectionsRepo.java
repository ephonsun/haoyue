package com.haoyue.repo;

import com.haoyue.pojo.Collections;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 */
public interface CollectionsRepo extends BaseRepo<Collections,Integer> {
    List<Collections> findByOpenIdAndSellerId(String openId, String sellerId);

    List<Collections> findBySellerIdAndCreateDate(String sellerId, Date date);
}
