package com.haoyue.repo;

import com.haoyue.pojo.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LiJia on 2017/10/31.
 */
public interface MemberRepo extends BaseRepo<Member,Integer> {
    Member findByOpenIdAndSellerId(String openId, String sellerId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update members set discount=?2 where seller_id=?1 and open_id is null")
    void updateDiscount(String sellerId, String discount);

    @Query(nativeQuery = true,value = "select discount from members where seller_id=?1 and open_id is null")
    String getDiscount(String sellerId);

    List<Member> findBySellerIdAndOpenIdIsNull(String sellerId);

    List<Member> findBySellerIdAndOpenIdIsNotNull(String sellerId);
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from members where  leavel='lev1' and open_id is not null")
    void delVip();

    List<Member> findByOpenIdIsNotNull();
}
