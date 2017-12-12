package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.*;
import com.haoyue.tuangou.repo.TuanOrdersRepo;
import com.haoyue.tuangou.utils.CommonUtil;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.wxpay.HttpRequest;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
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
                if (name.equals("groupcode")) {
                    bd.and(tuanorders.groupCode.eq(value));
                } else if (name.equals("isowner")) {
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
                if (name.equals("saleId")) {
                    bd.and(tuanorders.saleId.eq(value));
                } else if (name.equals("isowner")) {
                    bd.and(tuanorders.isowner.eq(Boolean.valueOf(value)));
                } else if (name.equals("state")) {
                    bd.and(tuanorders.state.eq(value));
                } else if (name.equals("openId")) {
                    bd.and(tuanorders.openId.eq(value));
                } else if (name.equals("isOver")) {
                    bd.and(tuanorders.isover.eq(Boolean.valueOf(value)));
                } else if (name.equals("showsale")) {
                    bd.and(tuanorders.showsale.eq(true));
                }
            }
        }

        return tuanOrdersRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));
    }

    public Iterable<TuanOrders> findTuanOrdersByTProducts(Integer pid, String saleId, String str) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.tProducts.id.eq(pid));
        bd.and(tuanorders.saleId.eq(saleId));
        if (!StringUtils.isNullOrBlank(str)) {
            bd.and(tuanorders.isowner.eq(true));
        }
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_tuaning));
        bd.and(tuanorders.isover.eq(false));
        bd.and(tuanorders.endDate.after(new Date()));
        return tuanOrdersRepo.findAll(bd.getValue());
    }


    public Iterable<TuanOrders> tuaning_clist(String saleId, String openId) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.saleId.eq(saleId));
        bd.and(tuanorders.openId.eq(openId));
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_tuaning));
        bd.and(tuanorders.isover.eq(false));
        bd.and(tuanorders.endDate.after(new Date()));
        return tuanOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TuanOrders> unsend(String saleId, String openId) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.saleId.eq(saleId));
        bd.and(tuanorders.openId.eq(openId));
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_success));
        bd.and(tuanorders.isover.eq(true));
        return tuanOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TuanOrders> finsh(String saleId, String openId) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.saleId.eq(saleId));
        bd.and(tuanorders.openId.eq(openId));
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_finsh));
        bd.and(tuanorders.isover.eq(true));
        bd.and(tuanorders.showbuy.eq(true));
        return tuanOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TuanOrders> comments(Map<String, String> map) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.iscomment.eq(true));
        bd.and(tuanorders.tProducts.id.eq(Integer.parseInt(map.get("pid"))));
        return tuanOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TuanOrders> query(Map<String, String> map) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        Date from = null;
        Date end = null;
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (!StringUtils.isNullOrBlank(value)) {
                if (key.equals("code")) {
                    bd.and(tuanorders.code.contains(value));
                }
                if (key.equals("saleId")) {
                    bd.and(tuanorders.saleId.eq(value));
                }
                if (key.equals("startDate")) {
                    try {
                        from = StringUtils.formatStrToDate((map.get("startDate")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (key.equals("endDate")) {
                    try {
                        end = StringUtils.formatStrToDate((map.get("endDate")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (from != null && end != null) {
                    bd.and(tuanorders.startDate.between(from, end));
                }
                if (key.equals("wxname")) {
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
        List<TuanOrders> list=tuanOrdersRepo.findByIsOver();
        Date date=new Date();
        for (TuanOrders orders:list){
            if (date.after(orders.getEndDate())){
                tuanOrdersRepo.flushdata(orders.getId());
                //通知
                addTemplate(orders);
            }
        }
    }

    public TuanOrders findByOut_trade_no(String out_trade_no) {
        return tuanOrdersRepo.findByOut_trade_no(out_trade_no);
    }

    public List<String> findOpenIdsByGroupCode(String groupcode) {
        return tuanOrdersRepo.findOpenIdsByGroupCode(groupcode);
    }

    public TuanOrders findByCode(String ordercode) {
        return tuanOrdersRepo.findByCode(ordercode);
    }

    public Iterable<TuanOrders> unreceive(String saleId, String openId) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.saleId.eq(saleId));
        bd.and(tuanorders.openId.eq(openId));
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_unreive));
        bd.and(tuanorders.isover.eq(true));
        return tuanOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TuanOrders> findUnPayBacks(Map<String, String> map, int pageNumber, int pageSize) {
        QTuanOrders tuanorders = QTuanOrders.tuanOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(tuanorders.isover.eq(true));
        bd.and(tuanorders.state.eq(TGlobal.tuan_order_tuaning));
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (!StringUtils.isNullOrBlank(value)) {
                if (key.equals("saleId")) {
                    bd.and(tuanorders.saleId.eq(value));
                }
                if (key.equals("openId")) {
                    bd.and(tuanorders.openId.eq(value));
                }
                if (key.equals("ispayback")) {
                    bd.and(tuanorders.ispayback.eq(Boolean.valueOf(value)));
                }
            }
        }
        return tuanOrdersRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));
    }


    // 系统自动收货 20 天
    public void autofinsh() {
        List<TuanOrders> list = tuanOrdersRepo.findByState(TGlobal.tuan_order_unreive);
        Date date = new Date();
        for (TuanOrders tuanOrder : list) {
            long start = tuanOrder.gettDeliver().getSendDate().getTime();
            //延迟收货 7 日
            if (tuanOrder.getIsdelay()) {
                start += 3600 * 24 * 7 * 1000;
            }
            if (date.getTime() - start > 3600 * 24 * 20 * 1000) {
                tuanOrder.setState(TGlobal.tuan_order_finsh);
                save(tuanOrder);
            }
        }
    }


    public void updateEndByPid(Integer id) {
        List<TuanOrders> list = tuanOrdersRepo.findTuaningByPid(id);
        tuanOrdersRepo.updateEndByPid(id);
        for (TuanOrders orders : list) {
            addTemplate(orders);
        }
    }

    //拼团失败通知
    public void addTemplate(TuanOrders order) {
        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(order.gettProducts().getPname());
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(String.valueOf(order.getTotalPrice()));
        list.add(templateResponse2);

        TemplateResponse templateResponse3 = new TemplateResponse();
        templateResponse3.setColor("#000000");
        templateResponse3.setName("keyword3");
        templateResponse3.setValue(TGlobal.tuan_nums_not_enough);
        list.add(templateResponse3);

        TemplateResponse templateResponse4 = new TemplateResponse();
        templateResponse4.setColor("#000000");
        templateResponse4.setName("keyword4");
        templateResponse4.setValue(TGlobal.tuan_comment);
        list.add(templateResponse4);

        Template template = new Template();
        template.setTemplateId("7mODyYjSYGbOxECnEyaKw9cLPiogmwjLvoIg8Tdi72I");
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage("pages/index/index");
        template.setToUser(order.getOpenId());
        getTemplate(template, order);
    }

    public void getTemplate(Template template, TuanOrders orders) {
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=wxf80175142f3214e1&secret=e0251029d53d21e84a650681af6139b1";
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        //发送模板信息
        String form_id = orders.getFormId();
        template.setForm_id(form_id);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + form_id;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("拼团订单刷新 result="+result);
    }

    //待退款订单
    public void autoPayback() {
        List<TuanOrders> list = tuanOrdersRepo.findUnPayback();
        if (list!=null&&list.size()!=0) {
            for (TuanOrders orders : list) {
                //拼接参数
                String param = "saleId=" + orders.getSaleId() + "&oid=" + orders.getId() + "&fe=" + orders.getTotalPrice()*100;
                //退款请求
                String result = HttpRequest.sendGet("https://www.cslapp.com/tuan/payback/do", param);
                //退款成功更新ispayback字段   在 t_paybackdeal 表中通过 result_code 判断成功还是失败
                if (!result.equalsIgnoreCase("fail")) {
                    tuanOrdersRepo.updatePayback(orders.getId());
                }
            }
        }
    }


}
