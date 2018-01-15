package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TFreeShopping;
import com.haoyue.tuangou.repo.TFreeShoppingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/28.
 */
@Service
public class TFreeShoppingService {

    @Autowired
    private TFreeShoppingRepo tFreeShoppingRepo;

    public void save(TFreeShopping freeShopping) {
        tFreeShoppingRepo.save(freeShopping);
    }

    public TFreeShopping findOne(int id) {
        return tFreeShoppingRepo.findOne(id);
    }

    public List<TFreeShopping> findBySaleIdAndOpenIdActive(String saleId, String openId) {
        return tFreeShoppingRepo.findBySaleIdAndOpenIdActive(saleId,openId);
    }

    public void autoFlush() {
        tFreeShoppingRepo.autoFlush(new Date());
    }
}
