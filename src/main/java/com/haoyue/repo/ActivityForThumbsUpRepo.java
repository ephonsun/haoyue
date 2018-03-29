package com.haoyue.repo;

import com.haoyue.pojo.ActivityForThumbsUp;

import java.util.List;

public interface ActivityForThumbsUpRepo extends BaseRepo<ActivityForThumbsUp,Integer> {
    ActivityForThumbsUp findByOpenIdAndIsowner(String openId, boolean b);

    List<ActivityForThumbsUp> findByIsowner(boolean flag);
}
