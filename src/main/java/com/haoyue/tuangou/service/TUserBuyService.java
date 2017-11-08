package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TUserBuy;
import com.haoyue.tuangou.repo.TUserBuyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/8.
 */
@Service
public class TUserBuyService {

    @Autowired
    private TUserBuyRepo tUserBuyRepo;

    public TUserBuy findByOpenIdAndSaleId(String openId, String saleId) {
        return tUserBuyRepo.findBySaleIdAndOpenId(saleId,openId);
    }

    public void update(TUserBuy userBuy) {
        tUserBuyRepo.save(userBuy);
    }
}
