package com.haoyue.repo;

import com.haoyue.pojo.CustomeCard;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Lijia on 2018/4/17.
 */
public interface CustomeCardRepo extends BaseRepo<CustomeCard,Integer> {
    List<CustomeCard> findBySellerIdAndOpenIdAndPid(String sellerId, String openId, String pid);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update customecard set active=false where active=true and open_id is not null and expire_date<?1")
    void flush(Date date);

    List<CustomeCard> findByActiveAndUsedAndOpenIdIsNotNull(boolean b, boolean b1);

    @Query(nativeQuery = true,value = "select remind from customecard where id=?1")
    boolean findRemindById(int i);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update customecard set hasremind=true where id=?1")
    void updateHasremind(Integer id);
}
