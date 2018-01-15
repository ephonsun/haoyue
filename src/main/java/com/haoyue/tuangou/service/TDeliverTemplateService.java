package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TDeliverTemplate;
import com.haoyue.tuangou.repo.TDeliverTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/11/7.
 */
@Service
public class TDeliverTemplateService {

    @Autowired
    private TDeliverTemplateRepo tDeliverTemplateRepo;

    public void save(TDeliverTemplate template) {
        tDeliverTemplateRepo.save(template);
    }

    public TDeliverTemplate findOne(Integer id) {
        return tDeliverTemplateRepo.findOne(id);
    }

    public void del(TDeliverTemplate deliverTemplate) {
        //删除关联表数据
        tDeliverTemplateRepo.deleteById(deliverTemplate.getId());
        //删除原来表数据
        tDeliverTemplateRepo.delete(deliverTemplate);
    }
}
