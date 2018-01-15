package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TVisitors;
import com.haoyue.tuangou.repo.TVisitorsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/10.
 */

@Service
public class TVisitorsService {

    @Autowired
    private TVisitorsRepo tVisitorsRepo;


    public TVisitors findBySaleIdAndOpenId(String saleId, String openId) {
        return  tVisitorsRepo.findBySaleIdAndOpenId(saleId,openId);
    }


    public void save(TVisitors tVisitors) {
        tVisitorsRepo.save(tVisitors);
    }
}
