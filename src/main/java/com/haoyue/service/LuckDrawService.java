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
    @Autowired
    private OrderService orderService;

    public void save(LuckDraw luckDraw) {
        //删除之前的抽奖数据
        luckDrawRepo.delBySellerId(luckDraw.getSellerId());
        //刷新订单 is_luck_draw_end
        orderService.updateIsLuckDrawEndBySeller(luckDraw.getSellerId());
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
