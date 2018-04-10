package com.haoyue.service;

import com.haoyue.pojo.IntegralRecord;
import com.haoyue.pojo.QIntegralRecord;
import com.haoyue.repo.IntegralRecordRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Lijia on 2018/4/10.
 */
@Service
public class IntegralRecordService {

    @Autowired
    private IntegralRecordRepo integralRecordRepo;

    public void save(IntegralRecord integralRecord) {
        integralRecordRepo.save(integralRecord);
    }

    public Iterable<IntegralRecord> list(Map<String, String> map, int pageNumber, int pageSize) {
        QIntegralRecord record = QIntegralRecord.integralRecord;
        BooleanBuilder bd = new BooleanBuilder();

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {

                if(name.equals("sellerId")){
                    bd.and(record.sellerId.eq(value));
                }
                else if(name.equals("openId")){
                    bd.and(record.openId.eq(value));
                }
                else if(name.equals("type")){
                    bd.and(record.type.eq(value));
                }

            }
        }
        return integralRecordRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,new String[]{"id"})));
    }
}
