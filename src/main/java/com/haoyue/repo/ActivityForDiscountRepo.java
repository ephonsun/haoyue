package com.haoyue.repo;

import com.haoyue.pojo.ActivityForDiscount;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Lijia on 2018/4/23.
 */
public interface ActivityForDiscountRepo extends BaseRepo<ActivityForDiscount,Integer> {

    @Query(nativeQuery = true,value = "select * from activity_discount where end_date<now() and active=true")
    List<ActivityForDiscount> findEndDateBefoe();
}
