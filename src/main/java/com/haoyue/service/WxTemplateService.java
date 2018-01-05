package com.haoyue.service;

import com.haoyue.pojo.WxTemplate;
import com.haoyue.repo.WxTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    //更新模板信息，是否失效,删除openid=undefined
    public void updateActive(){
        wxTemplateRepo.updateActive(new Date());
        wxTemplateRepo.deleteUndefined();
    }

    public List<String> findActive() {
        return wxTemplateRepo.findActive();
    }

    public List<WxTemplate> findByOpenId(String openid) {
        return wxTemplateRepo.findByOpenIdAndActive(openid);
    }
}
