package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TDeliver;
import com.haoyue.tuangou.repo.TDeliverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/9.
 */
@Service
public class TDeliverService {

    @Autowired
    private TDeliverRepo tDeliverRepo;

    public void save(TDeliver tdeliver) {
        tDeliverRepo.save(tdeliver);
    }

    public TDeliver findOne(Integer id) {
        return tDeliverRepo.findOne(id);
    }
}
