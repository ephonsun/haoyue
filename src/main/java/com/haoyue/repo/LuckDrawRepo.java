package com.haoyue.repo;

import com.haoyue.pojo.LuckDraw;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LiJia on 2017/11/3.
 */
public interface LuckDrawRepo extends BaseRepo<LuckDraw,Integer> {
    LuckDraw findBySellerId(String sellerId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from luckdraw where seller_id =?1")
    void delBySellerId(String sellerId);
}
