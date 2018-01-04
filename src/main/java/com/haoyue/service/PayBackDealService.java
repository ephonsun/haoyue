package com.haoyue.service;


import com.haoyue.repo.PayBackRepo;
import com.haoyue.wxpay.PayBackDeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2018/1/4.
 */
@Service
public class PayBackDealService {

    @Autowired
    private PayBackRepo payBackRepo;

    public void save(PayBackDeal payBackDeal){
        payBackRepo.save(payBackDeal);
    }

}
