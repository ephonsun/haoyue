package com.haoyue.tuangou.wxpay;

import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by LiJia on 2017/9/19.
 */
@RestController
@RequestMapping("/tuan/paydeal")
public class TPayDealController {

    @Autowired
    private TPayDealService TPayDealService;

    @RequestMapping("list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        Iterable iterable= TPayDealService.list(map,pageNumber,pageSize);
        return new Result(false, Global.do_success,iterable,null);
    }

}
