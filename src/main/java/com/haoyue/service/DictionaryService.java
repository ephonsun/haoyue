package com.haoyue.service;

import com.haoyue.pojo.Dictionary;
import com.haoyue.pojo.Products;
import com.haoyue.pojo.QDictionary;
import com.haoyue.repo.DictionaryRepo;
import com.haoyue.repo.SellerRepo;
import com.haoyue.repo.VisitorsRepo;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/24.
 */
@Service
public class DictionaryService {

    @Autowired
    private DictionaryRepo dictionaryRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private VisitorsService visitorsService;

    public Dictionary findByTokenAndName(String token, String name) {
        return null;
    }

    public Result save(Dictionary dictionary, String token) {
        if (dictionary.getId()!=null){
            if (dictionary.getSellerId()!=Integer.parseInt(token)){
                return new Result(true, Global.have_no_right,token);
            }
        }
        else {
            dictionary.setSellerId(Integer.parseInt(token));
            dictionary.setCreateDate(new Date());
            dictionary.setBuyers(0);
            dictionary.setViews(0);
            dictionary.setVisitors(0);
            dictionary.setTurnover(0.00);
        }
        return new Result(dictionaryRepo.save(dictionary),token);
    }

    public Result findBySellerId(int sid) {
        List<Dictionary> list=dictionaryRepo.findBySellerId(sid);
        if (list.size()==0){
            return new Result(false,Global.first_login,null,sid+"");
        }
        return new Result(list,sid+"");
    }

    public Iterable<Dictionary> findBySellerId2(int sid,Integer pageNumber,Integer pageSize) {
        QDictionary dictionary = QDictionary.dictionary;
        Date from=new Date();
        int month=from.getMonth()+1;
        if (month==1||month==3||month==5||month==7||month==9||month==11){
            pageSize=31;
        }else {
            pageSize=30;
        }
        Date to=new Date();
        from.setDate(1);
        from.setHours(0);
        from.setMinutes(0);
        from.setSeconds(0);
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(dictionary.sellerId.eq(sid));
        bd.and(dictionary.createDate.between(from,to));
        bd.and(dictionary.productId.isNull());
        return dictionaryRepo.findAll(bd.getValue(),new PageRequest(pageNumber, pageSize,new Sort(Sort.Direction.DESC,"id")));

    }

    public Dictionary findByProductId(Integer pid) {
        return dictionaryRepo.findByProductId(pid);
    }

    public void addEachDay() {
        //每天向dictionary表注入当日新的数据
        Dictionary dictionery=dictionaryRepo.findLast();
        Date date=StringUtils.getYMD(new Date());
       if ((!dictionery.getCreateDate().equals(date))){
          List<Integer> ids=sellerRepo.findIds();
           for (Integer id : ids){
             Dictionary dictionary=new Dictionary();
               dictionary.setTurnover(0.0);
               dictionary.setVisitors(0);
               dictionary.setViews(0);
               dictionary.setBuyers(0);
               dictionary.setSellerId(id);
               dictionary.setCreateDate(date);
               dictionaryRepo.save(dictionary);
           }
           //每日清空 visitors 表
           visitorsService.delAll();
       }
    }

    public Result del(Integer id) {
        dictionaryRepo.delete(id);
        return new Result(false,"",null);
    }

    public Dictionary findByDateAndSellerId(Date date, Integer sellerId) {
        return dictionaryRepo.findBySellerIdAndCreateDateAndProductIdIsNull(sellerId,StringUtils.getYMD(date));

    }

    public void update(Dictionary dictionary) {
        dictionaryRepo.save(dictionary);
    }

    public void addProduct(Products products) {
        Dictionary dictionary=new Dictionary();
        dictionary.setProductId(products.getId());
        dictionary.setTurnover(0.0);
        dictionary.setVisitors(0);
        dictionary.setBuyers(0);
        dictionary.setViews(0);
        dictionary.setCreateDate(new Date());
        dictionary.setSellerId(products.getSellerId());
        dictionaryRepo.save(dictionary);
    }
}
