package com.haoyue.hywebsite;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LiJia on 2018/1/12.
 */
@Service
public class TopicsService {

    @Autowired
    private TopicsRepo topicsRepo;

    public Topics findOne(int id) {
        return topicsRepo.findOne(id);
    }

    public Topics save(Topics topics) {
        return topicsRepo.save(topics);
    }

    public void del(int id) {
        Topics topic = findOne(id);
        if (topic.getPid() != null) {
            //删除中间表数据
            topicsRepo.delUnit(id);
        }
        topicsRepo.delete(id);
    }

    public Iterable<Topics> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTopics topics = QTopics.topics;
        BooleanBuilder bd = new BooleanBuilder();
        if (map.get("userId")!=null&&!map.get("userId").equals("")) {
            bd.and(topics.userid.eq(Integer.parseInt(map.get("userId"))));
        } else if (map.get("pid")!=null&&!map.get("pid").equals("")) {
            bd.and(topics.pid.isNotNull());
        } else if (map.get("pid")==null) {
            bd.and(topics.pid.isNull());
        }
        return topicsRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));
    }

    public void addViews(int id) {
        topicsRepo.addviews(id);
    }
}
