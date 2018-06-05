package com.haoyue.web;

import com.haoyue.pojo.WxTemplate;
import com.haoyue.service.WxTemplateService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by LiJia on 2017/11/29.
 */
@RestController
@RequestMapping("/wxtemplate")
public class WxTemplateController {

    @Autowired
    private WxTemplateService wxTemplateService;

    //   /wxtemplate/save?sellerId=3&openId=12&formId=12&buttonName=秒杀
    @RequestMapping("/save")
    private Result save(WxTemplate wxTemplate){
        wxTemplate.setCreateDate(new Date());
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,7);
        wxTemplate.setEndDate(calendar.getTime());
        wxTemplateService.save(wxTemplate);
        return new Result(false, Global.do_success,null,null);
    }

}
