package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.TDictionarys;
import com.haoyue.tuangou.repo.TDictionarysRepo;
import com.querydsl.core.BooleanBuilder;
import com.haoyue.tuangou.pojo.QTUserSale;
import com.haoyue.tuangou.pojo.TUserSale;
import com.haoyue.tuangou.repo.TUserSaleRepo;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/11/2.
 */
@Service("TUserSaleService")
public class TUserSaleService {

    @Autowired
    private TUserSaleRepo tUserSaleRepo;
    @Autowired
    private TDictionarysRepo tDictionarysRepo;


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
        //向tdictionarys表插入当日记录
        TDictionarys dictionarys=new TDictionarys();
        dictionarys.setCreateDate(new Date());
        dictionarys.setSaleId(tUserSale.getId()+"");
        tDictionarysRepo.save(dictionarys);

    }

    public Iterable<TUserSale> findOne(TUserSale tUserSale) {
        BooleanBuilder bd = new BooleanBuilder();
        QTUserSale sale = QTUserSale.tUserSale;
        if (!StringUtils.isNullOrBlank(tUserSale.getName())) {
            bd.and(sale.name.eq(tUserSale.getName()));
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getPhone())) {
            bd.and(sale.phone.eq(tUserSale.getPhone()));
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getEmail())) {
            bd.and(sale.email.eq(tUserSale.getEmail()));
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getPass())){
            bd.and(sale.pass.eq(tUserSale.getPass()));
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getId()+"")){
            bd.and(sale.id.eq(tUserSale.getId()));
        }
        return tUserSaleRepo.findAll(bd.getValue());
    }

    public Iterable<TUserSale> list(Map<String, String> map, int pageNumber, int pageSize) {
        BooleanBuilder bd = new BooleanBuilder();
        QTUserSale sale = QTUserSale.tUserSale;
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!StringUtils.isNullOrBlank(value)) {
                if (name.equals("name")){
                    bd.and(sale.name.eq(value));
                }
                if (name.equals("phone")){
                    bd.and(sale.phone.eq(value));
                }
                if (name.equals("email")){
                    bd.and(sale.email.eq(value));
                }

            }
        }


        return tUserSaleRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public TUserSale login(TUserSale tUserSale) {
        String name_phone_email=tUserSale.getName();
        String pass=tUserSale.getPass();
        TUserSale sale=tUserSaleRepo.findByNameAndPass(name_phone_email,pass);
        if (sale==null){
            sale=tUserSaleRepo.findByPhoneAndPass(name_phone_email,pass);
        }
        if (sale==null){
            sale=tUserSaleRepo.findByEmailAndPass(name_phone_email,pass);
        }
        if (sale==null){
            return null;
        }
        return sale;
    }

    public void update(TUserSale tUserSale) {
        TUserSale sale=tUserSaleRepo.findOne(tUserSale.getId());
        if (!StringUtils.isNullOrBlank(tUserSale.getEmail())){
            sale.setEmail(tUserSale.getEmail());
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getPass())){
            sale.setPass(tUserSale.getPass());
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getName())){
            sale.setName(tUserSale.getName());
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getPhone())){
            sale.setPhone(tUserSale.getPhone());
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getLunbo())){
            sale.setLunbo(tUserSale.getLunbo());
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getLunbo_products())){
            sale.setLunbo_products(tUserSale.getLunbo_products());
        }
        if (!StringUtils.isNullOrBlank(tUserSale.getIdentification())){
            sale.setIdentification(tUserSale.getIdentification());
        }
        tUserSaleRepo.save(sale);
    }

    public void update2(TUserSale userSale){
        tUserSaleRepo.save(userSale);
    }

    public TUserSale findOneById(int id){
        return tUserSaleRepo.findOne(id);
    }

    public List<TUserSale> findByOnlineCode(String onlinecode) {
        return tUserSaleRepo.findByOnlineCode(onlinecode);
    }

    public TUserSale findByPhone(String phone) {
        return tUserSaleRepo.findByPhone(phone);
    }
}
