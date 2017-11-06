package com.haoyue.web;

import com.haoyue.pojo.Customer;
import com.haoyue.pojo.ShopCar;
import com.haoyue.pojo.ShopCarDetail;
import com.haoyue.service.CustomerService;
import com.haoyue.service.ShopCarDetailService;
import com.haoyue.service.ShopCarService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public Result list(@RequestParam Map<String, String> map,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        //小程序端用户查看自己的购物车
        if(!StringUtils.isNullOrBlank(map.get("openId"))){
            Customer customer=customerService.findByOpenId(map.get("openId"),map.get("sellerId"));
            map.put("cid",customer.getId()+"");
            return  new Result(false, Global.do_success,customerService.list(map),null);
        }
        //卖家后台购物车
        else {
            Iterable<ShopCar> shopCars=shopCarService.list(map,pageNumber,pageSize);
            return  new Result(false, Global.do_success,shopCars,null);
        }
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
    public Result save(Integer proId,String openId,ShopCarDetail shopCarDetail,Integer sellerId,String wxname){
        ShopCar shopCar=new ShopCar();
        Customer customer=customerService.findByOpenId(openId,sellerId+"");
        shopCar.setCustomerId(customer.getId());
        shopCar.setSellerId(sellerId);
        shopCar.setCreateDate(new Date());
        shopCar.setWxname(wxname);
       return new Result(false,Global.do_success,shopCarService.save(shopCar,proId,shopCarDetail) ,null);
    }

    @RequestMapping("/shopcar_by_pro")
    public Result listByProducts(@RequestParam Map<String, String> map,String sellerId,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        List<Object> list=shopCarService.listByProducts(map,sellerId);
        List result=new ArrayList();
        double pagenumber=Math.ceil((list.size()/10.0));
        if (pageNumber==0){
            if (pageSize>list.size()){
                result=list;
            }else {
                result=list.subList(0,pageSize);
            }
        }else {
            if (pageNumber>pagenumber){
                return new Result(false,Global.pagenumber_not_right,null);
            }
            if (pageNumber*pageSize+pageSize>list.size()){
                result=list.subList(pageNumber*pageSize,list.size());
            }else {
                result = list.subList(pageNumber * pageSize, pageSize + pageNumber * pageSize);
            }
        }
        return new Result(false,Global.do_success,result ,pagenumber+"");
    }

    @RequestMapping("/findnames_by_pro")
    public Result findone_by_pro(String sellerId,String proId){
        List<String> names =shopCarService.findShopCarIdByProId(proId);
        return new Result(false,Global.do_success,names ,null);
    }


}
