package com.haoyue.fq.service;

import com.haoyue.fq.pojo.Income;
import com.haoyue.fq.pojo.QIncome;
import com.haoyue.fq.repo.IncomeRepo;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LiJia on 2017/12/25.
 */
@Service
public class IncomeService {

    @Autowired
    private IncomeRepo incomeRepo;

    public void save(Income income) {
        incomeRepo.save(income);
    }


    public Iterable<Income> findByOpenId(Map<String, String> map, int pageNumber, int pageSize) {
        QIncome income=QIncome.income;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(income.openId.eq(map.get("openId")));
        return incomeRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public void del(Integer incomeId) {
        incomeRepo.delete(incomeId);
    }

    public Income findOne(Integer incomeId) {
        return incomeRepo.findOne(incomeId);
    }
}
