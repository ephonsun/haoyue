package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.QTuanOrders;
import com.haoyue.tuangou.pojo.TuanOrders;
import com.haoyue.tuangou.repo.TuanOrdersRepo;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/11/9.
 */
@Service
public class TuanOrdersService {

    @Autowired
    private TuanOrdersRepo tuanOrdersRepo;

    public void save(TuanOrders tuanOrders) {
        tuanOrdersRepo.save(tuanOrders);
    }

    public Iterable<TuanOrders> filter(Map<String, String> map) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("groupcode")){
                    bd.and(tuanorders.groupCode.eq(value));
                }
                else if(name.equals("isowner")){
                    bd.and(tuanorders.isowner.eq(Boolean.valueOf(value)));
                }
            }
        }

        return tuanOrdersRepo.findAll(bd.getValue());
    }

    public TuanOrders findOne(int id) {
        return tuanOrdersRepo.findOne(id);
    }

    public List<TuanOrders> findByGroupCode(String groupcode) {
        return tuanOrdersRepo.findByGroupCode(groupcode);
    }

    public void update(TuanOrders tuanOrders) {
        tuanOrdersRepo.save(tuanOrders);
    }

    public void del(TuanOrders tuanOrders) {
        tuanOrdersRepo.delete(tuanOrders);
    }

    public Iterable<TuanOrders> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("saleId")){
                    bd.and(tuanorders.saleId.eq(value));
                }
                else if(name.equals("isowner")){
                    bd.and(tuanorders.isowner.eq(Boolean.valueOf(value)));
                }
                else if(name.equals("state")){
                    bd.and(tuanorders.state.eq(value));
                }
                else if(name.equals("openId")){
                    bd.and(tuanorders.openId.eq(value));
                }
                else if(name.equals("isOver")){
                    bd.and(tuanorders.isover.eq(Boolean.valueOf(value)));
                }
            }
        }

        return tuanOrdersRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public Iterable<TuanOrders> findTuanOrdersByTProducts(Integer pid, String saleId) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.tProducts.id.eq(pid));
        bd.and(tuanorders.saleId.eq(saleId));
        bd.and(tuanorders.isowner.eq(true));
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_tuaning));
        return tuanOrdersRepo.findAll(bd.getValue());
    }
}
