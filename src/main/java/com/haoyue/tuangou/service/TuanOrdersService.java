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

import java.text.ParseException;
import java.util.Date;
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

    public Iterable<TuanOrders> findTuanOrdersByTProducts(Integer pid, String saleId,String str) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.tProducts.id.eq(pid));
        bd.and(tuanorders.saleId.eq(saleId));
        if (!StringUtils.isNullOrBlank(str)){
             bd.and(tuanorders.isowner.eq(true));
        }
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_tuaning));
        return tuanOrdersRepo.findAll(bd.getValue());
    }


    public Iterable<TuanOrders> tuaning_clist(String saleId, String openId) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.saleId.eq(saleId));
        bd.and(tuanorders.openId.eq(openId));
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_tuaning));
        bd.and(tuanorders.isover.eq(false));
        return tuanOrdersRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"id"));
    }

    public Iterable<TuanOrders> unsend(String saleId, String openId) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.saleId.eq(saleId));
        bd.and(tuanorders.openId.eq(openId));
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_success));
        bd.and(tuanorders.isover.eq(true));
        return tuanOrdersRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"id"));
    }

    public Iterable<TuanOrders> finsh(String saleId, String openId) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.saleId.eq(saleId));
        bd.and(tuanorders.openId.eq(openId));
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_success));
        bd.and(tuanorders.isover.eq(true));
        bd.and(tuanorders.showbuy.eq(true));
        return tuanOrdersRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"id"));
    }

    public Iterable<TuanOrders> comments(Map<String, String> map, int pageNumber, int pageSize) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.iscomment.eq(true));
        bd.and(tuanorders.tProducts.id.eq(Integer.parseInt(map.get("pid"))));
        return tuanOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TuanOrders> query(Map<String, String> map) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        Date from=null;
        Date end=null;
        for (String key:map.keySet()){
            String value=map.get(key);
            if (!StringUtils.isNullOrBlank(value)){
                if (key.equals("code")){
                    bd.and(tuanorders.code.contains(value));
                }
                if (key.equals("startDate")){
                    try {
                        from=StringUtils.formatStrToDate((map.get("startDate")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (key.equals("endDate")){
                    try {
                        end=StringUtils.formatStrToDate((map.get("endDate")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (from!=null&&end!=null){
                    bd.and(tuanorders.startDate.between(from,end));
                }
                if (key.equals("wxname")){
                    bd.and(tuanorders.wxname.contains(value));
                }
            }
        }
        return tuanOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TuanOrders> commentslist(Map<String, String> map) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.iscomment.eq(true));
        bd.and(tuanorders.saleId.eq(map.get("saleId")));
        return tuanOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    //定时刷新团购订单状态
    public void flush() {
        tuanOrdersRepo.flushdata();
    }
}
