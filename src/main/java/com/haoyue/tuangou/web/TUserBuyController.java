package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TUserBuy;
import com.haoyue.tuangou.service.TCommentService;
import com.haoyue.tuangou.service.TOrdersService;
import com.haoyue.tuangou.service.TUserBuyService;
import com.haoyue.tuangou.service.TuanOrdersService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import com.haoyue.tuangou.wxpay.HttpRequest;
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
    @Autowired
    private TOrdersService ordersService;
    @Autowired
    private TuanOrdersService tuanOrdersService;
    @Autowired
    private TCommentService commentService;

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
        userBuy.setWxname(tUserBuy.getWxname());
        userBuy.setWxpic(tUserBuy.getWxpic());
        tUserBuyService.update(userBuy);
        return new TResult(false, TGlobal.do_success,userBuy);
    }

    @RequestMapping("/getSessionKey")
    public  TResult getOpenId(String appId,String code,String secret){
        String response= HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session","appid="+appId+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code");
        System.out.println(response);
        return new TResult(false,TGlobal.do_success,response);
    }

}
