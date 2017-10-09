package com.haoyue.service;

import com.haoyue.pojo.Visitors;
import com.haoyue.repo.VisitorsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/9/14.
 */
@Service
public class VisitorsService {

    @Autowired
    private VisitorsRepo visitorsRepo;

    public Visitors findBySellerIdAndOpenId(Integer sellerId, String openId) {
        return visitorsRepo.findBySellerIdAndOpenId(sellerId,openId);
    }

    public void save(Visitors visitors) {
        visitorsRepo.save(visitors);
    }

    public void delAll() {
        //visitorsRepo.deleteAll();效率低 sql 语句逐条发送逐条删除
        visitorsRepo.delAllData();//效率高，一条sql语句全部删除
    }

    public Visitors findByProductIdAndOpenId(int i, String openId) {
        return visitorsRepo.findByProductIdAndOpenId(i,openId);
    }
}
