package com.haoyue.repo;

import com.haoyue.pojo.Dictionary;
import com.haoyue.repo.BaseRepo;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/24.
 */
public interface DictionaryRepo extends BaseRepo<Dictionary ,Integer> {
    //Dictionary findByNameAndSellerId(String name, int i);

    List<Dictionary> findBySellerId(int sid);

    Dictionary findByProductId(Integer pid);

   // @Query(nativeQuery = true,value = "select * from dictionarys where seller_id=?1 and create_date bet")
    Dictionary findBySellerIdAndCreateDate(int token, Date ymd);

    @Query(nativeQuery = true,value = "select * from dictionarys order by id desc limit 1")
    Dictionary findLast();

    Dictionary findBySellerIdAndCreateDateAndProductIdIsNull(Integer sellerId, Date ymd);


}
