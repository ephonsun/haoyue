package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.QTAfterSale;
import com.haoyue.tuangou.pojo.TAfterSale;
import com.haoyue.tuangou.repo.TAfterSaleRepo;
import com.haoyue.tuangou.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LiJia on 2018/1/8.
 */
@Service
public class TAfterSaleService {

    @Autowired
    private TAfterSaleRepo aftersaleRepo;

    public TAfterSale save(TAfterSale afterSale) {
        return aftersaleRepo.save(afterSale);
    }

    public Iterable<TAfterSale> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTAfterSale aftersale=QTAfterSale.tAfterSale;
        BooleanBuilder bd=new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("saleId")){
                    bd.and(aftersale.saleId.eq(value));
                }else if (name.equals("openId")){
                    bd.and(aftersale.openId.eq(value));
                }else if (name.equals("state")){
                    bd.and(aftersale.isAgree.eq(value));
                }
            }
        }
        return aftersaleRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public TAfterSale findOne(int id){
        return aftersaleRepo.findOne(id);
    }
}
