package com.haoyue.tuangou.repo;

import com.haoyue.tuangou.pojo.TFreeShopping;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/28.
 */
public interface TFreeShoppingRepo extends TBaseRepo<TFreeShopping,Integer> {

    @Query(nativeQuery = true,value = "select * from t_free_shopping where sale_id=?1 and open_id=?2 and active=true")
    List<TFreeShopping> findBySaleIdAndOpenIdActive(String saleId, String openId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update t_free_shopping set is_active=false where is_active=true and end_date<?1")
    void autoFlush(Date date);
}
