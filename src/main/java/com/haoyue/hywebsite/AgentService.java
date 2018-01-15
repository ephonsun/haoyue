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
public class AgentService {

    @Autowired
    private AgentRepo agentRepo;

    public void save(Agent agent){
        agentRepo.save(agent);
    }

    public Agent findOne(int id){
        return agentRepo.findOne(id);
    }

    public void del(int id){
        agentRepo.delete(id);
    }

    public Iterable<Agent> list(Map<String, String> map, int pageNumber, int pageSize) {
        QAgent agent=QAgent.agent;
        BooleanBuilder bd=new BooleanBuilder();
        return agentRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public Agent findByManagerPhone(String phone){
        return agentRepo.findByManagerPhone(phone);
    }


}
