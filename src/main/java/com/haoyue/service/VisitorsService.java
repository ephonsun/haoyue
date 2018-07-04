package com.haoyue.service;

import com.haoyue.pojo.QVisitors;
import com.haoyue.pojo.Visitors;
import com.haoyue.repo.VisitorsRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LiJia on 2017/9/14.
 */
@Service
public class VisitorsService {

    @Autowired
    private VisitorsRepo visitorsRepo;

    public Visitors findBySellerIdAndOpenId(Integer sellerId, String openId) {
        return visitorsRepo.findBySellerIdAndOpenId(sellerId,openId);
    }

    public void save(Visitors visitors) {
        visitorsRepo.save(visitors);
    }

    public void delAll() {
        //visitorsRepo.deleteAll();效率低 sql 语句逐条发送逐条删除
        visitorsRepo.delAllData();//效率高，一条sql语句全部删除
    }

    public Visitors findByProductIdAndOpenId(int i, String openId) {
        return visitorsRepo.findByProductIdAndOpenId(i,openId);
    }

    public void update(Visitors visitors) {
        visitorsRepo.save(visitors);
    }

    public Iterable<Visitors> list(Map<String, String> map, int pageNumber, int pageSize) {

        QVisitors visitors=QVisitors.visitors;
        BooleanBuilder bd=new BooleanBuilder();
        for (String key:map.keySet()){
            String value=map.get(key);
            if(!StringUtils.isNullOrBlank(value)){
                if(key.equals("sellerId")){
                    bd.and(visitors.sellerId.eq(Integer.parseInt(value)));
                }

            }
        }
        return visitorsRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }
}
