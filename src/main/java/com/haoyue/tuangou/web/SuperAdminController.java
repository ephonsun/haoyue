package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TDictionarys;
import com.haoyue.tuangou.service.TDictionarysService;
import com.haoyue.tuangou.service.TuanOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by LiJia on 2017/11/14.
 */
@RequestMapping("/tuan/admin/")
public class SuperAdminController {

    @Autowired
    private TDictionarysService dictionarysService;
    @Autowired
    private TuanOrdersService tuanOrdersService;

    /**
     * 定时器，产品部署好之后，需要手动出发该定时器
     */
    @RequestMapping("/timer")
    public String timer(String key) {
        if (key.equals("abcdefg")) {
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            service.scheduleAtFixedRate(runnable, 60, 3600, TimeUnit.SECONDS);
            service.scheduleAtFixedRate(runnable2, 60, 60, TimeUnit.SECONDS);
            return "ok";
        }
        return "data_no_right";
    }

    // dictionary定时器 一个小时执行一次
    Runnable runnable = new Runnable() {
        public void run() {
            System.out.println("定时器执行了。。。。");
            //数据表 dictionarys 新增数据
           dictionarysService.addEachDay();
        }
    };

    //团购单定时器 一分钟刷新一次
    Runnable runnable2 = new Runnable() {
        public void run() {
            System.out.println("定时器执行了。。。。");
            tuanOrdersService.flush();
        }
    };



}
