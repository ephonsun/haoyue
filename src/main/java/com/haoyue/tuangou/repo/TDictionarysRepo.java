package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TDictionarys;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by LiJia on 2017/11/10.
 */
public interface TDictionarysRepo extends TBaseRepo<TDictionarys,Integer> {

    @Query(nativeQuery = true,value = "select * from t_dictionarys where sale_id=?1 and create_date=?2")
    TDictionarys findByTodaySaleId(String saleId, Date date);


    @Query(nativeQuery = true,value = "select * from t_dictionarys order by id desc limit 1")
    TDictionarys findByLastOne();

    TDictionarys findBySaleIdAndCreateDate(String saleId, Date date);
}
