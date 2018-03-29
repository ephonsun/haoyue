package com.haoyue.service;

import com.haoyue.pojo.MemberGroup;
import com.haoyue.repo.MemberGroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lijia on 2018/3/13.
 */
@Service
public class MemberGroupService {

    @Autowired
    private MemberGroupRepo memberGroupRepo;

    public void save(MemberGroup memberGroup) {
        memberGroupRepo.save(memberGroup);
    }

    public List<MemberGroup> findBySellerId(String sellerId) {
        return memberGroupRepo.findBySellerId(sellerId);
    }

    public void del(int id) {
        memberGroupRepo.delete(id);
    }
}
