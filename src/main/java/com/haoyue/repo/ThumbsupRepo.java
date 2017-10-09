package com.haoyue.repo;

import com.haoyue.pojo.Thumbsup;

/**
 * Created by LiJia on 2017/9/25.
 */
public interface ThumbsupRepo extends BaseRepo<Thumbsup,Integer> {
    Thumbsup findByProIdAndOpenId(String proId, String openId);
}
