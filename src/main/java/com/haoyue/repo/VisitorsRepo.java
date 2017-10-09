package com.haoyue.repo;

import com.haoyue.pojo.Visitors;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LiJia on 2017/9/14.
 */
public interface VisitorsRepo extends BaseRepo<Visitors,Integer> {

    Visitors findBySellerIdAndOpenId(Integer sellerId, String openId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete  from visitors")
    void delAllData();

    Visitors findByProductIdAndOpenId(int i, String openId);
}
