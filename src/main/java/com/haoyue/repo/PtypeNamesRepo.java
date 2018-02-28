package com.haoyue.repo;

import com.haoyue.pojo.PtypeNames;

import java.util.List;

/**
 * Created by LiJia on 2017/10/26.
 */
public interface PtypeNamesRepo extends BaseRepo<PtypeNames,Integer> {
    List<PtypeNames> findBySellerId(String token);

    PtypeNames findBySellerIdAndPtypename(String sellerId, String level2);

    PtypeNames findBySellerIdAndPtypenameIsNull(String sellerId);
}
