package com.haoyue.fq.repo;

import com.haoyue.fq.pojo.User;

/**
 * Created by LiJia on 2017/12/25.
 */
public interface UserRepo extends BaseRepo<User,Integer> {
    User findByOpenId(String openId);
}
