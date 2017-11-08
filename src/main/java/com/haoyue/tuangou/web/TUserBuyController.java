package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TUserBuy;
import com.haoyue.tuangou.service.TUserBuyService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by LiJia on 2017/11/8.
 */
@RestController
@RequestMapping("/tuan/userbuy")
public class TUserBuyController {

    @Autowired
    private TUserBuyService tUserBuyService;

    @RequestMapping("/login")
    public TResult regOrLogin(TUserBuy tUserBuy) {
        TUserBuy userBuy = tUserBuyService.findByOpenIdAndSaleId(tUserBuy.getOpenId(), tUserBuy.getSaleId());
        if (userBuy == null) {
            userBuy = new TUserBuy();
            userBuy.setCreateDate(new Date());
            userBuy.setSaleId(tUserBuy.getSaleId());
            userBuy.setCity(tUserBuy.getCity());
            userBuy.setGender(tUserBuy.getGender());
            userBuy.setOpenId(tUserBuy.getOpenId());
        }
        userBuy.setWxpic(tUserBuy.getWxpic());
        userBuy.setWxname(tUserBuy.getWxname());
        tUserBuyService.update(userBuy);
        return new TResult(false, TGlobal.do_success,userBuy);
    }

}
