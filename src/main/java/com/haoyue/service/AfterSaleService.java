package com.haoyue.service;

import com.haoyue.pojo.AfterSale;
import com.haoyue.pojo.QAfterSale;
import com.haoyue.repo.AfterSaleRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    public Iterable<AfterSale> list(Map<String, String> map, int pageNumber, int pageSize) {
        QAfterSale aftersale=QAfterSale.afterSale;
        BooleanBuilder bd=new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
               if (name.equals("sellerId")){
                   bd.and(aftersale.sellerId.eq(value));
               }else if (name.equals("openId")){
                   bd.and(aftersale.openId.eq(value));
               }else if (name.equals("state")){
                   bd.and(aftersale.isAgree.eq(value));
               }
            }
        }
        return afterSaleRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }
}
