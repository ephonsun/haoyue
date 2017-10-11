package com.haoyue.repo;

import com.haoyue.pojo.Deliver;
import com.haoyue.repo.BaseRepo;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LiJia on 2017/8/24.
 */
public interface DelievrRepo extends BaseRepo<Deliver,Integer> {
    Deliver findByDcodeAndDename(String dcode, String dename);

    @Query(nativeQuery = true,value = "select  * from deliver where seller_id=?1 group by dname")
    List<Deliver> findBySellerId(String sellerId);

    @Query(nativeQuery = true,value = "select distinct  dname from deliver where seller_id=?1")
    List<String> findDnamesBySellerId(String sellerId);

    List<Deliver>  findBySellerIdAndDname(String sellerId, String dname);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from deliver where seller_id=?2 and dname=?1")
    void deleteByDnameAndSellerId(String dname, String sellerId);
}
