package com.haoyue.service;

import com.haoyue.pojo.CustomProductsTypes;
import com.haoyue.pojo.QActivityForDiscount;
import com.haoyue.pojo.QCustomProductsTypes;
import com.haoyue.repo.CustomProductsTypesRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/12/8.
 */
@Service
public class CustomProductsTypesService {

    @Autowired
    private CustomProductsTypesRepo customProductsTypesRepo;

    public void save(CustomProductsTypes customProductsTypes) {
        customProductsTypesRepo.save(customProductsTypes);
    }

    public CustomProductsTypes findOne(int id) {
        return customProductsTypesRepo.findOne(id);
    }

    public void del(CustomProductsTypes customProductsTypes) {
        customProductsTypesRepo.delete(customProductsTypes);
    }

    public List<CustomProductsTypes> findBySellerId(String sellerId) {
        return customProductsTypesRepo.findBySellerId(sellerId);
    }

    public void update(CustomProductsTypes parent) {
        customProductsTypesRepo.save(parent);
    }

    public Iterable<CustomProductsTypes> list(Map<String, String> map, int pageNumber, int pageSize) {
        QCustomProductsTypes types = QCustomProductsTypes.customProductsTypes;
        BooleanBuilder bd = new BooleanBuilder();
        Date date = new Date();
        bd.and(types.pid.isNull());
        bd.and(types.active.eq(true));
        if (StringUtils.isNullOrBlank(map.get("lunbo"))) {
            bd.and(types.name.ne("轮播图"));
        }else {
            bd.and(types.name.eq("轮播图"));
        }
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("sellerId")) {
                    bd.and(types.sellerId.eq(value));
                }

            }
        }
        return customProductsTypesRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, new String[]{"id"})));
    }


    public void del_middle(CustomProductsTypes customProductsTypes) {
        customProductsTypesRepo.del_middle(customProductsTypes.getId());
    }
}
