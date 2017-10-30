package com.haoyue.web;

import com.haoyue.pojo.Customer;
import com.haoyue.pojo.ShopCar;
import com.haoyue.pojo.ShopCarDetail;
import com.haoyue.service.CustomerService;
import com.haoyue.service.ShopCarDetailService;
import com.haoyue.service.ShopCarService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * Created by LiJia on 2017/9/4.
 */
@RestController
@RequestMapping("/shopCar")
public class ShopCarController {

    @Autowired
    private ShopCarService shopCarService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ShopCarDetailService shopCarDetailService;

    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map){
        Customer customer=customerService.findByOpenId(map.get("openId"),map.get("sellerId"));
        map.put("cid",customer.getId()+"");
        return  new Result(false, Global.do_success,customerService.list(map),null);
    }

    @RequestMapping("/del")
    public Result del(Integer id){
        try {
            shopCarService.del(id);
        }catch (Exception e){
            return new Result(false,Global.record_unexist,null,null);
        }
        return new Result(false,Global.do_success,null,null);
    }

    @RequestMapping("/update")
    public Result update(ShopCarDetail shopCarDetail){
        shopCarDetailService.update(shopCarDetail);
        return new Result(false,Global.do_success,null,null);
    }

    @RequestMapping("/save")
    public Result save(Integer proId,String openId,ShopCarDetail shopCarDetail,Integer sellerId){
        ShopCar shopCar=new ShopCar();
        Customer customer=customerService.findByOpenId(openId,sellerId+"");
        shopCar.setCustomerId(customer.getId());
        shopCar.setSellerId(sellerId);
        shopCar.setCreateDate(new Date());
       return new Result(false,Global.do_success,shopCarService.save(shopCar,proId,shopCarDetail) ,null);
    }


}
