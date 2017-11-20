package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TWxTemplate;
import com.haoyue.tuangou.repo.TWxTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/17.
 */
@Service
public class TWxTemplateService {

    @Autowired
    private TWxTemplateRepo wxTemplateRepo;


    public void save(TWxTemplate wxTemplate) {
        wxTemplateRepo.save(wxTemplate);
    }
}
