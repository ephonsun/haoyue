package com.haoyue.service;

import com.haoyue.pojo.LeaveMessage;
import com.haoyue.pojo.QLeaveMessage;
import com.haoyue.repo.LeaveMessageRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LiJia on 2017/9/30.
 */
@Service
public class LeaveMessageService {

    @Autowired
    private LeaveMessageRepo leaveMessageRepo;

    public LeaveMessage save(LeaveMessage leaveMessage) {
       return leaveMessageRepo.save(leaveMessage);
    }

    public Iterable<LeaveMessage> list(Map<String, String> map, int pageNumber, int pageSize) {

        QLeaveMessage leavemessage=QLeaveMessage.leaveMessage;
        BooleanBuilder boolenbuild=new BooleanBuilder();

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {

            }
        }
        return leaveMessageRepo.findAll(boolenbuild.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"createDate")));
    }

    public void del(String id) {
        leaveMessageRepo.delete(Integer.parseInt(id));
    }
}
