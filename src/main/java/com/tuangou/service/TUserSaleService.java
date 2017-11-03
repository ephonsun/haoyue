package com.tuangou.service;

import com.querydsl.core.BooleanBuilder;
import com.tuangou.pojo.QTUserSale;
import com.tuangou.pojo.TUserSale;
import com.tuangou.repo.TUserSaleRepo;
import com.tuangou.utils.StringUtils;
import com.tuangou.utils.TGlobal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by LiJia on 2017/11/2.
 */
@Service
public class TUserSaleService {

    @Autowired
    private TUserSaleRepo tUserSaleRepo;


    public void save(TUserSale tUserSale) {
        tUserSale.setCreateDate(new Date());
        String authority = tUserSale.getAuthority();
        if (authority.equals("0")) {
            tUserSale.setMaxFile(TGlobal.max_FileSzie);
        } else if (authority.equals("1")) {
            tUserSale.setMaxFile(TGlobal.max_FileSzie * 5);
        } else if (authority.equals("2")) {
            tUserSale.setMaxFile(TGlobal.max_FileSzie * 10);
        }
        tUserSaleRepo.save(tUserSale);
    }

    public Iterable<TUserSale> findOne(TUserSale tUserSale) {
        BooleanBuilder bd=new BooleanBuilder();
        QTUserSale sale=QTUserSale.tUserSale;
        if (!StringUtils.isNullOrBlank(tUserSale.getName())){
            bd.and(sale.name.eq(tUserSale.getName()));
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getPhone())){
            bd.and(sale.phone.eq(tUserSale.getPhone()));
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getEmail())){
            bd.and(sale.email.eq(tUserSale.getEmail()));
        }
        return tUserSaleRepo.findAll(bd.getValue());
    }
}
