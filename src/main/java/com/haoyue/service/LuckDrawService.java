package com.haoyue.service;

import com.haoyue.pojo.LuckDraw;
import com.haoyue.repo.LuckDrawRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/3.
 */
@Service
public class LuckDrawService {

    @Autowired
    private LuckDrawRepo luckDrawRepo;

    public void save(LuckDraw luckDraw) {
        luckDrawRepo.delBySellerId(luckDraw.getSellerId());
        luckDrawRepo.save(luckDraw);
    }

    public LuckDraw findBySellerId(String sellerId){
        LuckDraw luckDraw= luckDrawRepo.findBySellerId(sellerId);
        return luckDraw;
    }

    public void update(LuckDraw luckDraw) {
        luckDrawRepo.save(luckDraw);
    }


}
