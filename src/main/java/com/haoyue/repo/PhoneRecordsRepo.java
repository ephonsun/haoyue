package com.haoyue.repo;

import com.haoyue.pojo.PhoneRecords;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Lijia on 2018/7/3.
 */
public interface PhoneRecordsRepo extends BaseRepo<PhoneRecords,Integer> {

    @Query(nativeQuery = true,value = "select * from phone_records where seller_id=?1 and phone=?2")
    List<PhoneRecords> findBySellerIdAndPhone(String sellerId, String phone);
}
