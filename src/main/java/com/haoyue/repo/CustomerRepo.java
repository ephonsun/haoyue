package com.haoyue.repo;

import com.haoyue.pojo.Customer;
import com.haoyue.repo.BaseRepo;

import java.util.List;

/**
 * Created by LiJia on 2017/9/4.
 */
public interface CustomerRepo extends BaseRepo<Customer,Integer> {
    List<Customer> findByPhone(String phone);

    Customer findByOpenId(String openId);

    Customer findByOpenIdAndSellerId(String openId, String sellerId);

    List<Customer> findByWxnameAndSellerId(String wxname, String sellerId);

    Customer findByWebOpenIdAndSellerId(String webOpenId, String sellerId);
}
