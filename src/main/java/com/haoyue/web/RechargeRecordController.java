package com.haoyue.web;

import com.haoyue.pojo.RechargeRecord;
import com.haoyue.service.RechargeRecordService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Lijia on 2018/6/4.
 */

@RestController
@RequestMapping("/recharge")
public class RechargeRecordController {

    @Autowired
    private RechargeRecordService rechargeRecordService;


    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<RechargeRecord> iterable=rechargeRecordService.list(map,pageNumber,pageSize);
        return new Result(false, Global.do_success, iterable, null);
    }


}
