package com.haoyue.tuangou.service;

import com.haoyue.tuangou.repo.TPayBackDealRepo;
import com.haoyue.tuangou.wxpay.TPayBackDeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/17.
 */

@Service
public class TPayBackDealService {

    @Autowired
    private TPayBackDealRepo tPayBackDealRepo;

    public void save(TPayBackDeal payBackDeal) {
        tPayBackDealRepo.save(payBackDeal);
    }
}
