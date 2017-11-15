package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TuanOrders;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/9.
 */
public interface TuanOrdersRepo extends TBaseRepo<TuanOrders,Integer> {
    List<TuanOrders> findByGroupCode(String groupcode);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_tuanorders set isover=true where end_date < ?1 and isover=false ")
    void flushdata();
}
