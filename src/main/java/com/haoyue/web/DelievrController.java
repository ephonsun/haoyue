package com.haoyue.web;

import com.haoyue.pojo.Deliver;
import com.haoyue.pojo.Order;
import com.haoyue.service.DelievrService;
import com.haoyue.service.OrderService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/8/24.
 */

@RestController
@RequestMapping("/deliver")
public class DelievrController {

    @Autowired
    private DelievrService  delievrService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/save")
    public Result deliver(Integer oid,Deliver deliver,String token){
        Order order=orderService.findOne(oid);
        if (order.getSellerId()!=Integer.parseInt(token)){
            return new Result(true,Global.have_no_right,null,token);
        }
        //判断快递单号-快递名是否存在
        Deliver deliver2=delievrService.findByDcodeAndDename(deliver.getDcode(),deliver.getDename());
        if (deliver2!=null){
            return new Result(true,Global.record_exist,null,null);
        }
        Deliver deliver1=order.getDeliver();
        deliver.setId(deliver1.getId());
        delievrService.update(deliver);
        order.setDeliver(deliver);
        orderService.update(order);
        return new Result(false,Global.do_success,order,token);
    }
}
