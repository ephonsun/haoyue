package com.haoyue.web;

import com.haoyue.pojo.Customer;
import com.haoyue.pojo.Visitors;
import com.haoyue.service.CustomerService;
import com.haoyue.service.VisitorsService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/9/14.
 */
@RestController
@RequestMapping("/visitors")
public class VisitorsController {

    @Autowired
    private VisitorsService visitorsService;
    @Autowired
    private CustomerService customerService;

    @RequestMapping("/del")
    public Result delAll(){
        visitorsService.delAll();
        return new Result(false, Global.do_success,null,null);
    }

    // /visitors/list?sellerId=3&pageNumber=页数(从0开始)&pageSize=默认10
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        Iterable<Visitors> iterable= visitorsService.list(map,pageNumber,pageSize);
        return new Result(false, Global.do_success,iterable,null);
    }

}
