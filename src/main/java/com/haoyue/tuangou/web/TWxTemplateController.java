package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TWxTemplate;
import com.haoyue.tuangou.service.TWxTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by LiJia on 2017/11/17.
 */

@RestController
@RequestMapping("/tuan/wxtemplate")
public class TWxTemplateController {

    @Autowired
    private TWxTemplateService wxTemplateService;

    @RequestMapping("/save")
    public void save(TWxTemplate wxTemplate){
        wxTemplate.setCreateDate(new Date());
        wxTemplateService.save(wxTemplate);
    }

}
