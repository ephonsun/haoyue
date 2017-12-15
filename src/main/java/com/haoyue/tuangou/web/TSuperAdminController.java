package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TDictionarys;
import com.haoyue.tuangou.pojo.TRedPacket;
import com.haoyue.tuangou.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by LiJia on 2017/11/14.
 */

@RestController
@RequestMapping("/tuan/admin/")
public class TSuperAdminController {

    @Autowired
    private TDictionarysService dictionarysService;
    @Autowired
    private TuanOrdersService tuanOrdersService;
    @Autowired
    private TRedPacketService redPacketService;
    @Autowired
    private TCouponService couponService;
    @Autowired
    private TProductsService productsService;
    @Autowired
    private TFreeShoppingService freeShoppingService;

    /**
     * 定时器，产品部署好之后，需要手动触发该定时器
     * http://localhost:8080/tuan/admin/timer?key=abcdefg&saleId=1
     * https://www.cslapp.com/tuan/admin/timer?key=abcdefg&saleId=1
     */
    @RequestMapping("/timer")
    public String timer(String key) {
        if (key.equals("abcdefg")) {
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            service.scheduleAtFixedRate(runnable, 60, 3600, TimeUnit.SECONDS);
            service.scheduleAtFixedRate(runnable2, 120, 300, TimeUnit.SECONDS);
            service.scheduleAtFixedRate(runnable3, 240, 300, TimeUnit.SECONDS);
            service.scheduleAtFixedRate(runnable4, 360, 3600, TimeUnit.SECONDS);
           // service.scheduleAtFixedRate(runnable5, 420, 3600, TimeUnit.SECONDS);
            return "ok";
        }
        return "data_no_right";
    }

    // dictionary定时器 一个小时执行一次
    Runnable runnable = new Runnable() {
        public void run() {
            System.out.println("刷新团购dictionarys表定时器执行了。。。。");
            //数据表 dictionarys 新增数据
           dictionarysService.addEachDay();
        }
    };

    //团购单定时器 10分钟刷新一次
    Runnable runnable2 = new Runnable() {
        public void run() {
            System.out.println("刷新团购订单状态定时器执行了。。。。");
            tuanOrdersService.flush();
            //自动退款
            tuanOrdersService.autoPayback();
        }
    };

    //红包定时器 10分钟刷新一次
    Runnable runnable3 = new Runnable() {
        public void run() {
            System.out.println("刷新团购红包状态定时器执行了。。。。");
            redPacketService.flush();
        }
    };


    //优惠券定时器  一个小时刷新一次
    Runnable runnable4 = new Runnable() {
        public void run() {
            System.out.println("刷新团购优惠券状态定时器执行了。。。。");
            couponService.flush();
        }
    };

    //团购商品定时器  一个小时刷新一次
    Runnable runnable5 = new Runnable() {
        public void run() {
            System.out.println("刷新团购商品状态定时器执行了。。。。");
            productsService.autoFlushEnd();
            System.out.println("刷新零元购状态定时器执行了。。。。");
            freeShoppingService.autoFlush();
        }
    };


}
