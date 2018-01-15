package com.haoyue.hywebsite;

/**
 * Created by LiJia on 2018/1/12.
 */
public interface TouristRepo extends BaseRepo<Tourist,Integer> {
    Tourist findByUsernameAndPassword(String username, String password);

    Tourist findByPhone(String phone);

    Tourist findByUsername(String username);
}
