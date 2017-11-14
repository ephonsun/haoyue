package com.haoyue.tuangou.web;


import com.haoyue.tuangou.pojo.TDictionarys;
import com.haoyue.tuangou.pojo.TVisitors;
import com.haoyue.tuangou.service.TDictionarysService;
import com.haoyue.tuangou.service.TVisitorsService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/10.
 */

@RestController
@RequestMapping("/tuan/tdictionary")
public class TDictionarysController {

    @Autowired
    private TDictionarysService tDictionarysService;
    @Autowired
    private TVisitorsService tVisitorsService;


    //   /tuan/tdictionary/update?saleId=12&openId=12&wxname=微信名
    @RequestMapping("/update")
    public TResult addViews(String saleId, String openId, String wxname) {
        synchronized (TGlobal.object3) {
            //判断当日记录是否存在
            TDictionarys tDictionarys = tDictionarysService.findByTodaySaleId(saleId);
            if (tDictionarys == null) {
                tDictionarysService.addEachDay();
                //重新查找一次
                tDictionarys = tDictionarysService.findByTodaySaleId(saleId);
            }
            //浏览量
            tDictionarys.setViews(tDictionarys.getViews() + 1);
            //访客数
            TVisitors tVisitors = tVisitorsService.findBySaleIdAndOpenId(saleId, openId);
            if (tVisitors == null) {
                tVisitors = new TVisitors();
                tVisitors.setCreateDate(new Date());
                tVisitors.setOpenId(openId);
                tVisitors.setSaleId(saleId);
                tVisitors.setWxname(wxname);
                tVisitorsService.save(tVisitors);
                tDictionarys.setVisitors(tDictionarys.getVisitors() + 1);
            }
            tDictionarysService.update(tDictionarys);
        }
        return new TResult(false, TGlobal.do_success, null);
    }


    //   /tuan/tdictionary/list?saleId=12
    @RequestMapping("/list")
    public TResult list(String saleId){
        Iterable<TDictionarys>  list= tDictionarysService.list(saleId);
        return new TResult(false, TGlobal.do_success, list);
    }

}
