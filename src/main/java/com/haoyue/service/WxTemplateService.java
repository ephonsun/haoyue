package com.haoyue.service;

import com.haoyue.pojo.WxTemplate;
import com.haoyue.repo.WxTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by LiJia on 2017/11/29.
 */
@Service
public class WxTemplateService {

    @Autowired
    private WxTemplateRepo wxTemplateRepo;

    public void save(WxTemplate wxTemplate) {
        wxTemplateRepo.save(wxTemplate);
    }


    // 删除过期信息
    public void del(String sellerId){
        wxTemplateRepo.delActiveFalse(sellerId);
    }

    //更新模板信息，是否失效
    public void updateActive(){
        wxTemplateRepo.updateActive(new Date());
    }

}
