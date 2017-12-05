package com.haoyue.web;

import com.haoyue.pojo.AfterSale;
import com.haoyue.pojo.Dictionary;
import com.haoyue.pojo.Order;
import com.haoyue.service.AfterSaleService;
import com.haoyue.service.DictionaryService;
import com.haoyue.service.OrderService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by LiJia on 2017/9/20.
 * 售后
 */

@RestController
@RequestMapping("/after-sale")
public class AfterSaleController {

    @Autowired
    private AfterSaleService afterSaleService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DictionaryService dictionaryService;

    //http://localhost:8080/after-sale/save?oid=133&openId=1111&sellerId=1
    @RequestMapping("/save")
    public Result save(String oid, AfterSale afterSale) {
        Order order=orderService.findOne(Integer.parseInt(oid));
        if (order.getSellerId()!=Integer.parseInt(afterSale.getSellerId())){
            return new Result(true,Global.have_no_right,null,null);
        }
        //更新订单是否申请退换货
        order.setIsApplyReturn(true);
        orderService.update(order);
        afterSale.setOrder(order);
        return new Result(false, Global.do_success, afterSaleService.save(afterSale), null);
    }

    //http://localhost:8080/after-sale/deal?id=4&token=1&isAgree=yes
    @RequestMapping("/deal")
    public Result deal(String id,String token,String isAgree){
        AfterSale afterSale=afterSaleService.findOne(id);
        if (!token.equals(afterSale.getSellerId())){
            return new Result(true,Global.have_no_right,null,null);
        }
        if (isAgree.equals("yes")){
            afterSale.setIsAgree("yes");
            Order order=afterSale.getOrder();
            Double totalPrice=order.getTotalPrice();
            Date date=order.getCreateDate();
            Dictionary dictionary=dictionaryService.findByDateAndSellerId(date,Integer.parseInt(token));
            dictionary.setTurnover(dictionary.getTurnover()-totalPrice);
            dictionaryService.update(dictionary);
        }else {
            afterSale.setIsAgree("no");
        }
        afterSaleService.update(afterSale);
        return new Result(false,Global.do_success,null,null);
    }


}
