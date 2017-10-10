package com.haoyue.repo;

import com.haoyue.pojo.Deliver;
import com.haoyue.repo.BaseRepo;

/**
 * Created by LiJia on 2017/8/24.
 */
public interface DelievrRepo extends BaseRepo<Deliver,Integer> {
    Deliver findByDcodeAndDename(String dcode, String dename);
}
