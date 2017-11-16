package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.QTDictionarys;
import com.haoyue.tuangou.pojo.TDictionarys;
import com.haoyue.tuangou.pojo.TUserSale;
import com.haoyue.tuangou.repo.TDictionarysRepo;
import com.haoyue.tuangou.repo.TUserSaleRepo;
import com.haoyue.tuangou.repo.TVisitorsRepo;
import com.haoyue.tuangou.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/10.
 */
@Service
public class TDictionarysService {

    @Autowired
    private TDictionarysRepo tDictionarysRepo;
    @Autowired
    private TUserSaleRepo tUserSaleRepo;
    @Autowired
    private TVisitorsRepo tVisitorsRepo;

    public TDictionarys findByTodaySaleId(String saleId) {
        Date now=new Date();
        Date date=StringUtils.getYMD(now);
        return tDictionarysRepo.findByTodaySaleId(saleId,date);
    }

    public void update(TDictionarys tDictionarys) {
        tDictionarysRepo.save(tDictionarys);
    }

    public void addEachDay(){
        TDictionarys tDictionarys=tDictionarysRepo.findByLastOne();
        Date date=new Date();
        //添加当日数据
        if (!StringUtils.getYMD(date).equals(tDictionarys.getCreateDate())){
            List<Integer> ids= tUserSaleRepo.findAllIds();
            for (Integer id:ids){
                TDictionarys dictionarys=new TDictionarys();
                dictionarys.setSaleId(String.valueOf(id));
                dictionarys.setCreateDate(date);
                tDictionarysRepo.save(dictionarys);
            }
            //删除visitor
            tVisitorsRepo.delAll();
        }

    }

    public Iterable<TDictionarys> list(String saleId) {
        QTDictionarys dictionarys=QTDictionarys.tDictionarys;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(dictionarys.saleId.eq(saleId));
        int pageNumber=0;
        int pageSize=37;
        return tDictionarysRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }
}
