package com.haoyue.repo;

import com.haoyue.pojo.CashTicket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LiJia on 2017/11/1.
 */
public interface CashTicketRepo extends BaseRepo<CashTicket,Integer>{

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete  from cashtickets where seller_id=?1 and open_id is null")
    void deleteBySellerIdAndOpenIdIsNull(String sellerId);

    List<CashTicket> findBySellerIdAndOpenIdIsNull(String sellerId);

    CashTicket findByCode(String cashTicketCode);
}
