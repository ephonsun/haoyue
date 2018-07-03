package com.haoyue.repo;

import com.haoyue.pojo.PhoneRecords;

import java.util.List;

/**
 * Created by Lijia on 2018/7/3.
 */
public interface PhoneRecordsRepo extends BaseRepo<PhoneRecords,Integer> {
    List<PhoneRecords> findBySellerIdAndPhone(String sellerId, String phone);
}
