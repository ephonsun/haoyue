package com.haoyue.repo;

import com.haoyue.pojo.ProductDailyRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 */
public interface ProductDailyRecordRepo extends BaseRepo<ProductDailyRecord,Integer> {
    ProductDailyRecord findByPidAndSellerIdAndCreateDate(int pid, String sellerId, Date date);

    List<ProductDailyRecord> findBySellerIdAndCreateDate(String sellerId, Date date);
}
