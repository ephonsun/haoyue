package com.haoyue.service;

import com.haoyue.pojo.AfterSale;
import com.haoyue.repo.AfterSaleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LiJia on 2017/9/20.
 */
@Service
public class AfterSaleService {

    @Autowired
    private AfterSaleRepo afterSaleRepo;

    public AfterSale save(AfterSale afterSale) {
        return afterSaleRepo.save(afterSale);
    }

    public AfterSale findOne(String id) {
        return afterSaleRepo.findOne(Integer.parseInt(id));
    }

    public void update(AfterSale afterSale) {
        afterSaleRepo.save(afterSale);
    }
}
