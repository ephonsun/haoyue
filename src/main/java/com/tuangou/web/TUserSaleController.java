package com.tuangou.web;

import com.tuangou.pojo.TUserSale;
import com.tuangou.service.TUserSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by LiJia on 2017/11/2.
 */
@RestController
public class TUserSaleController {

    @Autowired
    private TUserSaleService tUserSaleService;

    public void save(TUserSale tUserSale){
        tUserSaleService.save(tUserSale);
    }

    public void findOne(TUserSale tUserSale){
        Iterable<TUserSale> iterable=tUserSaleService.findOne(tUserSale);
       return;
    }





}
