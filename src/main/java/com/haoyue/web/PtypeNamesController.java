package com.haoyue.web;

import com.haoyue.pojo.PtypeNames;
import com.haoyue.service.PtypeNamesService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/10/26.
 */
@RestController
@RequestMapping("/ptypename")
public class PtypeNamesController {

    @Autowired
    private PtypeNamesService ptypeNamesService;

    @RequestMapping("/save")
    public Result save(PtypeNames ptypeNames){
        ptypeNamesService.save(ptypeNames);
        return new Result(false, Global.do_success,null,null);
    }

    @RequestMapping("/list")
    public Result list(String sellerId){
        String token=sellerId;
        PtypeNames ptypeNames= ptypeNamesService.findBySellerId(token);
        return new Result(false,Global.do_success,ptypeNames,token);
    }

    //衬衫,连衣裙,衬衫,标题,爱迪生,qwe,123,213,456,234
    //http://localhost:8080/ptypename/save?sellerId=1&ptypename=衬衫,连衣裙,衬衫,标题,爱迪生,qwe,123,213,456,234
    // https://www.cslapp.com/ptypename/save?sellerId=1&ptypename=裤子,毛衣啊,风衣,夹克
    //  https://www.cslapp.com/ptypename/save?sellerId=3&ptypename=连衣裙,大衣,休闲裙,西装外套,衬衫,半裙,毛衣
    // https://www.cslapp.com/ptypename/save?sellerId=4&ptypename=汽车美容,车载导航,汽车贴膜,汽车灯具
    // https://www.cslapp.com/ptypename/save?sellerId=5&ptypename=测试
}
