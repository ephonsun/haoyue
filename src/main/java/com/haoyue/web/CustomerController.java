package com.haoyue.web;

import com.haoyue.pojo.Customer;
import com.haoyue.pojo.Seller;
import com.haoyue.service.*;
import com.haoyue.untils.Global;
import com.haoyue.untils.HttpRequest;
import com.haoyue.untils.Result;
import com.haoyue.untils.WXAppletUserInfo;
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
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ProductsService productsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommentService commentService;


    //关键词查询 商品分类查询
    @RequestMapping("/proSearch")
    public Result proSearch(@RequestParam Map<String, String> map){
       // map.put("showdate","yes");
       return new Result(false, Global.do_success,productsService.list(map), map.get("token")) ;
    }

    /*
    购物车可以点击查看物品详情
    购物车列表显示物品分类，尺码，颜色
    */

    @RequestMapping("/loginOrReg")
    public Result save(String openId,String sellerId,String wxname,String wxpic){

        // boolean flag=sellerService.isStop(sellerId);
        Seller seller=sellerService.findOne(Integer.parseInt(sellerId));
        boolean flag=seller.getIsActive();
        boolean isCoupon=seller.getIscoupon();
        //判断该店铺服务是否停止
        if (!flag){
            return new Result(true,Global.service_stop,null,null);
        }
        //优惠券活动是否开启
        if (isCoupon){
            return new Result(true,Global.coupon_isopen,null,null);
        }
        if (openId.equals("undefined")){
            return new Result(true,Global.openId_undefined,null,null);
        }
        Customer customer=customerService.findByOpenId(openId,sellerId);
        if (customer==null){
            customer=new Customer();
            customer.setOpenId(openId);
            customer.setSellerId(sellerId);
            customer.setCreateDate(new Date());
            customer.setWxname(wxname);
            customer.setWxpic(wxpic);
            customer=customerService.save(customer);
        }else {
            customer.setWxname(wxname);
            customer.setWxpic(wxpic);
            customerService.update(customer);
            //更改订单中微信名和头像
            orderService.updateWxname(wxname,customer.getId());
            //更新评论中微信名和头像
            commentService.updateWxname(wxname,wxpic,customer.getOpenId(),customer.getSellerId());

        }
        return new Result(false,Global.do_success,customer,null);
    }

    //  /customer/getSessionKey?appId=小程序ID&code=12&secret=小程序secret
    @RequestMapping("/getSessionKey")
    public  Result getOpenId(String appId,String code,String secret){
        String response= HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session","appid="+appId+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code");
        System.out.println(response);
        return new Result(false,null,response,null);
    }

    @RequestMapping("/getPhone")
    public Result getPhone(String encryptedData,String iv,String session_key){
        WXAppletUserInfo wxAppletUserInfo=new WXAppletUserInfo();
        String response=wxAppletUserInfo.decodeUserInfo(encryptedData,iv,session_key);
        System.out.println("response==="+response);
        return  new Result(false,Global.do_success,response,null);
    }



}
