package com.haoyue.repo;

import com.haoyue.pojo.ActivityForThumbsUp;

public interface ActivityForThumbsUpRepo extends BaseRepo<ActivityForThumbsUp,Integer> {
    ActivityForThumbsUp findByOpenIdAndIsowner(String openId, boolean b);
}
