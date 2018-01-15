package com.haoyue.fq.web;

import com.haoyue.fq.pojo.Income;
import com.haoyue.fq.service.IncomeService;
import com.haoyue.fq.utils.Global;
import com.haoyue.fq.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * Created by LiJia on 2017/12/25.
 */
@RestController
@RequestMapping("/fq/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    //  /fq/income/save?type=来源&money=金额&comment=备注
    @RequestMapping("/save")
    public Result save(Income income){
        if (income.getId()==null) {
            income.setCreateDate(new Date());
        }
        incomeService.save(income);
        return new Result(false, Global.do_success,null);
    }

    //  /fq/income/list?openId=1212&pageNumber=页数(从0开始)
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        Iterable<Income> iterable= incomeService.findByOpenId(map,pageNumber,pageSize);
        return new Result(false, Global.do_success,iterable);
    }

    //  /fq/income/del?incomeId=记录的ID
    @RequestMapping("/del")
    public Result del(Integer incomeId){
        incomeService.del(incomeId);
        return new Result(false, Global.do_success,null);
    }


    //  /fq/income/findone?incomeId=记录的ID
    @RequestMapping("/findone")
    public Result findOne(Integer incomeId){
        Income income=incomeService.findOne(incomeId);
        return new Result(false, Global.do_success,income);
    }


}
