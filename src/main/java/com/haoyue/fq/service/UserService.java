package com.haoyue.fq.service;

import com.haoyue.fq.pojo.User;
import com.haoyue.fq.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/12/25.
 */
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User findByOpenId(String openId) {
        return userRepo.findByOpenId(openId);
    }

    public void save(User user1) {
        userRepo.save(user1);
    }
}
