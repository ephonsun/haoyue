package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TDeliver;
import com.haoyue.tuangou.pojo.TOrders;
import com.haoyue.tuangou.service.TDeliverService;
import com.haoyue.tuangou.service.TOrdersService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/11/9.
 */
@RestController
@RequestMapping("/tuan/tdeliver")
public class TDeliverController {

    @Autowired
    private TDeliverService tDeliverService;
    @Autowired
    private TOrdersService tOrdersService;

    // /tuan/tdeliver/update?id=物流ID&dcode=单号&dname=物流名&saleId=123&oid=订单ID
    @RequestMapping("/update")
    public TResult update(TDeliver deliver, String saleId,String oid){
        TDeliver tDeliver=tDeliverService.findOne(deliver.getId());
        if (!saleId.equals(tDeliver.getSaleId1())){
            return new TResult(true, TGlobal.have_no_right,null);
        }
        tDeliver.setDcode(deliver.getDcode());
        tDeliver.setDname(deliver.getDname());
        tDeliverService.save(tDeliver);
        //修改订单状态
        TOrders orders=tOrdersService.findOne(Integer.parseInt(oid));
        orders.setState(TGlobal.order_unreceive);
        tOrdersService.update(orders);
        return new TResult(false, TGlobal.do_success,tDeliver);
    }

}
