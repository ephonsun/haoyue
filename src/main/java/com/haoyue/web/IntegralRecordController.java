package com.haoyue.web;

import com.haoyue.pojo.Integral;
import com.haoyue.pojo.IntegralRecord;
import com.haoyue.service.IntegralRecordService;
import com.haoyue.service.IntegralService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Lijia on 2018/4/10.
 */
@RestController
@RequestMapping("/integralRecord")
public class IntegralRecordController {

    @Autowired
    private IntegralRecordService integralRecordService;

    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        Iterable<IntegralRecord> iterable= integralRecordService.list(map,pageNumber,pageSize);
        return new Result(false, Global.do_success,iterable,null);
    }

}
