package com.haoyue.repo;

import com.haoyue.pojo.PtypeNames;

/**
 * Created by LiJia on 2017/10/26.
 */
public interface PtypeNamesRepo extends BaseRepo<PtypeNames,Integer> {
    PtypeNames findBySellerId(String token);
}
