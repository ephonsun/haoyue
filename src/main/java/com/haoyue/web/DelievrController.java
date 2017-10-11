package com.haoyue.web;

import com.haoyue.pojo.Deliver;
import com.haoyue.pojo.Order;
import com.haoyue.service.DelievrService;
import com.haoyue.service.OrderService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by LiJia on 2017/8/24.
 */

@RestController
@RequestMapping("/deliver")
public class DelievrController {

    @Autowired
    private DelievrService delievrService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/save")
    public Result deliver(Integer oid, Deliver deliver, String token) {
        Order order = orderService.findOne(oid);
        if (order.getSellerId() != Integer.parseInt(token)) {
            return new Result(true, Global.have_no_right, null, token);
        }
        //判断快递单号-快递名是否存在
        Deliver deliver2 = delievrService.findByDcodeAndDename(deliver.getDcode(), deliver.getDename());
        if (deliver2 != null) {
            return new Result(true, Global.record_exist, null, null);
        }
        Deliver deliver1 = order.getDeliver();
        deliver.setId(deliver1.getId());
        delievrService.update(deliver);
        order.setDeliver(deliver);
        orderService.update(order);
        return new Result(false, Global.do_success, order, token);
    }

    @Transactional
    @RequestMapping("/save_template")
    public Result seller_template(String sellerId, String delivers) {

        String lines[] = delivers.split("-");
        String line[] = null;
        String destination = null;
        int account = 1;
        double price = 0.0;
        int more_account = 1;
        double more_price = 0.0;
        String dname = null;
        int index=1;
        for (int i = 0; i < lines.length; i++) {
            Deliver deliver = new Deliver();
            line = lines[i].split(",");

            if (!isDiget(line[2])) {
                return new Result(true, Global.account_unright, null, null);
            }
            if (!isDiget(line[3])) {
                return new Result(true, Global.price_unright, null, null);
            }
            if (!isDiget(line[4])) {
                return new Result(true, Global.more_account_unright, null, null);
            }
            if (!isDiget(line[5])) {
                return new Result(true, Global.more_price_unright, null, null);
            }

            dname = line[0];
            destination = line[1];
            account = Integer.parseInt(line[2]);
            price = Integer.parseInt(line[3]);
            more_account = Integer.parseInt(line[4]);
            more_price = Double.valueOf(line[5]);

            deliver.setSellerId(sellerId);
            deliver.setDname(dname);
            deliver.setDestination(destination);
            deliver.setAccount(account);
            deliver.setPrice(price);
            deliver.setMore_account(more_account);
            deliver.setMore_price(more_price);
            //判断是不是更新
            if (index==1){
                index++;
                delievrService.deleteByDnameAndSellerId(dname,sellerId);
            }
            delievrService.save2(deliver);
        }

        return new Result(false, Global.do_success, null, null);
    }

    public boolean isDiget(String str) {
        if (StringUtils.isNullOrBlank(str)){
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping("/getTemplate")
    public Result getTamplate(String sellerId,String dname){
        if (StringUtils.isNullOrBlank(dname)) {
            List deliverList = delievrService.findBySellerId(sellerId);
            return new Result(false, Global.do_success, deliverList, null);
        }
        else {
            List<Deliver> list=delievrService.findBySellerIdAndDname(sellerId,dname);
            return new Result(false,Global.do_success,list,null);
        }
    }

}
