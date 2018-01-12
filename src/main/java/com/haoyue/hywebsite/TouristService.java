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
public class TouristService {

    @Autowired
    private TouristRepo touristRepo;

    public void save(Tourist tourist) {
        touristRepo.save(tourist);
    }

    public Tourist findByUsernameAndPassword(String username, String password) {
        return touristRepo.findByUsernameAndPassword(username,password);
    }

    public Iterable<Tourist> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTourist tourist=QTourist.tourist;
        BooleanBuilder bd=new BooleanBuilder();
        return touristRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public Tourist findOne(int id) {
        return touristRepo.findOne(id);
    }

    public void del(int id) {
        touristRepo.delete(id);
    }

    public Tourist findByPhone(String phone){
        return touristRepo.findByPhone(phone);
    }

    public Tourist findByUsername(String username){
        return touristRepo.findByUsername(username);
    }
}
