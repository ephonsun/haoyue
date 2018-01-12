package com.haoyue.hywebsite;

/**
 * Created by LiJia on 2018/1/12.
 */
public interface AgentRepo extends BaseRepo<Agent,Integer> {
    Agent findByManagerPhone(String phone);
}
