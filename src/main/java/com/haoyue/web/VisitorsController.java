package com.haoyue.web;

import com.haoyue.service.VisitorsService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/9/14.
 */
@RestController
@RequestMapping("/visitors")
public class VisitorsController {

    @Autowired
    private VisitorsService visitorsService;

    @RequestMapping("/del")
    public Result delAll(){
        visitorsService.delAll();
        return new Result(false, Global.do_success,null,null);
    }

}
