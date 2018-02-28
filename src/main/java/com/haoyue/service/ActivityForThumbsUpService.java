package com.haoyue.service;

import com.haoyue.pojo.ActivityForThumbsUp;
import com.haoyue.pojo.QActivityForThumbsUp;
import com.haoyue.repo.ActivityForThumbsUpRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ActivityForThumbsUpService {

    @Autowired
    private ActivityForThumbsUpRepo activityForThumbsUpRepo;

    public ActivityForThumbsUp findOne(int pid) {
        return activityForThumbsUpRepo.findOne(pid);
    }

    public void save(ActivityForThumbsUp activityForThumbsUp) {
        activityForThumbsUpRepo.save(activityForThumbsUp);
    }

    public ActivityForThumbsUp findOpenIdAndIsowner(String openId, boolean b) {
        return activityForThumbsUpRepo.findByOpenIdAndIsowner(openId,b);
    }

    public Iterable<ActivityForThumbsUp> list(Map<String, String> map, int pageNumber, int pageSize) {

        QActivityForThumbsUp qActivityForThumbsUp=QActivityForThumbsUp.activityForThumbsUp;
        BooleanBuilder bd=new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("isowner")){
                    bd.and(qActivityForThumbsUp.isowner.eq(Boolean.valueOf(value)));
                }
            }
        }
        return activityForThumbsUpRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,new String[]{"id"})));
    }

    public void delete(ActivityForThumbsUp old) {
        activityForThumbsUpRepo.delete(old);
    }
}
