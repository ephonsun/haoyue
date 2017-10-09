package com.haoyue.service;

import com.haoyue.pojo.BasicComments;
import com.haoyue.pojo.QBasicComments;
import com.haoyue.repo.BasicCommentsRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by LiJia on 2017/9/25.
 */
@Service
public class BasicCommentsService {

    @Autowired
    private BasicCommentsRepo basicCommentsRepo;

    public void save(BasicComments basicComments) {
        basicComments.setCreateDate(new Date());
        basicCommentsRepo.save(basicComments);
    }

    public Iterable<BasicComments> list(Map<String, String> map, int pageNumber, int pageSize) {
        QBasicComments basiccomments = QBasicComments.basicComments;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("proId")) {
                    bd.and(basiccomments.proId.eq(value));
                }
            }
        }
        return basicCommentsRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "createDate")));

    }
}
