package com.haoyue.repo;

import com.haoyue.pojo.ProductDailyRecord;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 */
public interface ProductDailyRecordRepo extends BaseRepo<ProductDailyRecord,Integer> {
    ProductDailyRecord findByPidAndSellerIdAndCreateDate(int pid, String sellerId, Date date);

    List<ProductDailyRecord> findBySellerIdAndCreateDate(String sellerId, Date date);

    @Query(nativeQuery =true,value = "select * from product_daily_record where pid=?1 and create_date between ?2 and ?3")
    List<ProductDailyRecord> findByPidLastMonth(int pid, Date time, Date now);
}
