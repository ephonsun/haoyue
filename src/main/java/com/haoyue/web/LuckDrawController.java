package com.haoyue.web;

import com.haoyue.pojo.LuckDraw;
import com.haoyue.service.LuckDrawService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by LiJia on 2017/11/3.
 */
@RestController
@RequestMapping("/luckdraw")
public class LuckDrawController {

    @Autowired
    private LuckDrawService luckDrawService;

    // http://localhost:8080/luckdraw/save?sellerId=1&allNumber=1000&lackNumber=[中奖号码以=号分隔,1=2=3=4]
    @RequestMapping("/save")
    public Result save(LuckDraw luckDraw){
        luckDraw.setCreateDate(new Date());
        luckDrawService.save(luckDraw);
        return new Result(false, Global.do_success,luckDraw,null);
    }

    // http://localhost:8080/luckdraw/findOne?sellerId=1
    @RequestMapping("/findOne")
    public Result findOne(String sellerId){
        LuckDraw luckDraw=luckDrawService.findBySellerId(sellerId);
        return new Result(false, Global.do_success,luckDraw,null);
    }



}
