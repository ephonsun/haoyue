package com.haoyue.service;

import com.haoyue.pojo.ActivityForDiscount;
import com.haoyue.pojo.QActivityForDiscount;
import com.haoyue.pojo.QCustomeCard;
import com.haoyue.repo.ActivityForDiscountRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Lijia on 2018/4/23.
 */
@Service
public class ActivityForDiscountService {

    @Autowired
    private ActivityForDiscountRepo activityForDiscountRepo;

    public void save(ActivityForDiscount activityForDiscount) {
        activityForDiscountRepo.save(activityForDiscount);
    }

    public ActivityForDiscount findOne(int activityId) {
        return activityForDiscountRepo.findOne(activityId);
    }

    public void update(ActivityForDiscount activityForDiscount) {
        activityForDiscountRepo.save(activityForDiscount);
    }

    public Iterable<ActivityForDiscount> list(Map<String, String> map, int pageNumber, int pageSize) {

        QActivityForDiscount activity =QActivityForDiscount.activityForDiscount;
        BooleanBuilder bd = new BooleanBuilder();
        Date date=new Date();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {

                if(name.equals("sellerId")){
                    bd.and(activity.sellerId.eq(value));
                }
                else if(name.equals("active")){
                    bd.and(activity.active.eq(Boolean.valueOf(value)));
                }
                else if(name.equals("before")){
                    bd.and(activity.fromDate.after(date));
                }
                else if(name.equals("after")){
                    bd.and(activity.endDate.before(date));
                }
                else if(name.equals("between")){
                    bd.and(activity.fromDate.before(date));
                    bd.and(activity.endDate.after(date));
                }
            }
        }
        return activityForDiscountRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,new String[]{"id"})));
    }

    public List<ActivityForDiscount> findEndDateBefor() {
       return activityForDiscountRepo.findEndDateBefoe();
    }
}
