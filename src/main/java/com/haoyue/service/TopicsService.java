package com.haoyue.service;

import com.haoyue.pojo.QTopics;
import com.haoyue.pojo.Topics;
import com.haoyue.repo.TopicsRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Lijia on 2018/5/4.
 */

@Service
public class TopicsService {

    @Autowired
    private TopicsRepo topicsRepo;


    public void save(Topics topics) {
        topicsRepo.save(topics);
    }

    public Iterable<Topics> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTopics topics=QTopics.topics;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(topics.active.eq(true));
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if(name.equals("sellerId")){
                    bd.and(topics.sellerId.eq(value));
                }
            }
        }
        return topicsRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public Topics findOne(Integer id) {
        return topicsRepo.findOne(id);
    }

    public void update(Topics topics) {
        topicsRepo.save(topics);
    }
}
