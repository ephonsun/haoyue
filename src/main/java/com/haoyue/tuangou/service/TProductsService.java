package com.haoyue.tuangou.service;


import com.haoyue.tuangou.pojo.QTProducts;
import com.haoyue.tuangou.pojo.TProducts;
import com.haoyue.tuangou.repo.TProductsRepo;
import com.haoyue.tuangou.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/11/7.
 */
@Service
public class TProductsService {

    @Autowired
    private TProductsRepo tProductsRepo;

    public void save(TProducts tProducts) {
        tProductsRepo.save(tProducts);
    }

    public void update(TProducts tProducts) {
        tProductsRepo.save(tProducts);
    }

    public Iterable<TProducts> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTProducts products=QTProducts.tProducts;
        BooleanBuilder bd=new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("saleId")){
                    bd.and(products.saleId.eq(value));
                }
                else if (name.equals("active")){
                    bd.and(products.active.eq(Boolean.valueOf(value)));
                    if (value.equals("true")) {
                        bd.and(products.productsTypes.any().active.eq(true));
                    }
                }
            }
        }
        return tProductsRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public TProducts findOne(Integer id) {
        return tProductsRepo.findOne(id);
    }

    public List<TProducts> findByTuanProduct(String saleId) {
        return  tProductsRepo.findByTuanProduct(saleId);
    }
}
