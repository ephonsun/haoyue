package com.haoyue.service;

import com.haoyue.pojo.WxTemplate;
import com.haoyue.repo.WxTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
