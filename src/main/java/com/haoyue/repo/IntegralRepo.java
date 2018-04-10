package com.haoyue.repo;

import com.haoyue.pojo.Integral;

/**
 * Created by Lijia on 2018/4/8.
 */
public interface IntegralRepo extends BaseRepo<Integral,Integer> {
    Integral findBySellerIdAndTypename(String s, String s1);
}
