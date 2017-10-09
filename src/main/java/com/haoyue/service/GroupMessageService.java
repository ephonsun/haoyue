package com.haoyue.service;

import com.haoyue.pojo.GroupMessage;
import com.haoyue.repo.GroupMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/9/7.
 */
@Service
public class GroupMessageService {

    @Autowired
    private GroupMessageRepo groupMessageRepo;

    public GroupMessage save(GroupMessage groupMessage) {
        return groupMessageRepo.save(groupMessage);
    }

    public GroupMessage findOne(Integer id) {
        return groupMessageRepo.findOne(id);
    }
}
