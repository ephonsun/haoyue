package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TVisitors;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LiJia on 2017/11/10.
 */
public interface TVisitorsRepo extends TBaseRepo<TVisitors,Integer> {
    TVisitors findBySaleIdAndOpenId(String saleId, String openId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete  from t_visitors")
    void delAll();
}
