package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TDeliverTemplates;
import com.haoyue.tuangou.repo.TDeliverTemplatesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LiJia on 2017/11/7.
 */
@Service
public class TDeliverTemplatesService {

    @Autowired
    private TDeliverTemplatesRepo templatesRepo;

    public void save(TDeliverTemplates templates) {
        templatesRepo.save(templates);
    }

    public TDeliverTemplates findOne(Integer id) {
        return templatesRepo.findOne(id);
    }

    public void del(TDeliverTemplates tDeliverTemplates) {
        templatesRepo.delete(tDeliverTemplates);
    }

    public List<TDeliverTemplates> findBySaleId(String saleId) {
        return templatesRepo.findBySaleId(saleId);
    }

    public TDeliverTemplates findBySaleIdAndDname(String saleId, String dname) {
        return templatesRepo.findBySaleIdAndDname(saleId,dname);
    }
}
