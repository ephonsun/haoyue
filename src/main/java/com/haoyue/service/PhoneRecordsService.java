package com.haoyue.service;

import com.haoyue.pojo.PhoneRecords;
import com.haoyue.pojo.QPhoneRecords;
import com.haoyue.repo.PhoneRecordsRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Lijia on 2018/7/3.
 */
@Service
public class PhoneRecordsService {

    @Autowired
    private PhoneRecordsRepo phoneRecordsRepo;

    public void save(PhoneRecords phoneRecords) {
        phoneRecordsRepo.save(phoneRecords);
    }

    public Iterable<PhoneRecords> list(Map<String, String> map, int pageNumber, int pageSize) {
        QPhoneRecords phoneRecords=QPhoneRecords.phoneRecords;
        BooleanBuilder bd=new BooleanBuilder();
        for (String key:map.keySet()){
            String value=map.get(key);
            if(!StringUtils.isNullOrBlank(value)){
                if(key.equals("sellerId")){
                    bd.and(phoneRecords.sellerId.eq(value));
                }
            }
        }
        return phoneRecordsRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public PhoneRecords findOne(String id) {
        return phoneRecordsRepo.findOne(Integer.parseInt(id));
    }

    public void delete(String id) {
        phoneRecordsRepo.delete(Integer.parseInt(id));
    }

    public List<PhoneRecords> findBySellerIdAndPhone(String sellerId, String phone) {
        return phoneRecordsRepo.findBySellerIdAndPhone(sellerId,phone);
    }
}
