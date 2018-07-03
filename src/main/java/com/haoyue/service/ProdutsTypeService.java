package com.haoyue.service;

import com.haoyue.pojo.ProdutsType;
import com.haoyue.pojo.QProdutsType;
import com.haoyue.repo.ProdutsTypeRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/8/23.
 */
@Service
public class ProdutsTypeService {

    @Autowired
    private ProdutsTypeRepo produtsTypeRepo;

    public Iterable<ProdutsType> list(Map<String, String> map , int pageNumber, int pageSize) {

        QProdutsType ptype = QProdutsType.produtsType;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("token")){
                    bd.and(ptype.sellerId.eq(Integer.parseInt(value)));
                }
            }
        }
         return produtsTypeRepo.findAll(bd.getValue(),new PageRequest(pageNumber, pageSize));
    }

    public ProdutsType save(ProdutsType produtsType) {
        produtsTypeRepo.save(produtsType);
        return produtsType;
    }

    public ProdutsType findOne(Integer id) {
        return produtsTypeRepo.findOne(id);
    }

    public void del(ProdutsType produtsType) {
        produtsTypeRepo.delete(produtsType);
    }

    public void update(List<ProdutsType> produtsTypes) {
        produtsTypeRepo.save(produtsTypes);
    }

    public void updateDiscount(Integer id) {
        produtsTypeRepo.updateDiscount(id);
    }
}
