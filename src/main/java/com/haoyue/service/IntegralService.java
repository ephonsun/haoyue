package com.haoyue.service;

import com.haoyue.pojo.Integral;
import com.haoyue.pojo.QIntegral;
import com.haoyue.repo.IntegralRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Lijia on 2018/4/8.
 */
@Service
public class IntegralService {

    @Autowired
    private IntegralRepo integralRepo;

    public void save(Integral integral) {
        integralRepo.save(integral);
    }

    public Integral findById(int id) {
        return integralRepo.findOne(id);
    }

    public Iterable<Integral> list(Map<String, String> map) {
        QIntegral integral=QIntegral.integral;
        BooleanBuilder bd=new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("sellerId")){
                   bd.and(integral.sellerId.eq(value));
                }else if(name.equals("typename")){
                    bd.and(integral.typename.eq(value));
                }
            }
        }
        return integralRepo.findAll(bd.getValue());
    }
}
