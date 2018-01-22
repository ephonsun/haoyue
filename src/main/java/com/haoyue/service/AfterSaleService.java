package com.haoyue.service;

import com.haoyue.pojo.AfterSale;
import com.haoyue.pojo.Message;
import com.haoyue.pojo.QAfterSale;
import com.haoyue.repo.AfterSaleRepo;
import com.haoyue.untils.HttpRequest;
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
 * Created by LiJia on 2017/9/20.
 */
@Service
public class AfterSaleService {

    @Autowired
    private AfterSaleRepo afterSaleRepo;
    @Autowired
    private MessageService messageService;

    public AfterSale save(AfterSale afterSale) {
        afterSaleRepo.save(afterSale);
        //保存协商信息
        String str = afterSale.getOrder().getTotalPrice() + "#" + afterSale.getReason() + "#" + afterSale.getPhone() + "#" + afterSale.getOrder().getState() + "#" + afterSale.getDescs();
        messageService.save(str, afterSale.getId());
        return afterSale;
    }

    public AfterSale findOne(String id) {
        return afterSaleRepo.findOne(Integer.parseInt(id));
    }

    public void update(AfterSale afterSale, String str) {
        afterSaleRepo.save(afterSale);
        //保存协商信息
        if (!StringUtils.isNullOrBlank(str)) {
            messageService.save(str, afterSale.getId());
        }
    }


    public Iterable<AfterSale> list(Map<String, String> map, int pageNumber, int pageSize) {
        QAfterSale aftersale = QAfterSale.afterSale;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("sellerId")) {
                    bd.and(aftersale.sellerId.eq(value));
                } else if (name.equals("openId")) {
                    bd.and(aftersale.openId.eq(value));
                } else if (name.equals("state")) {
                    bd.and(aftersale.isAgree.eq(value));
                } else if (name.equals("active")) {
                    bd.and(aftersale.active.eq(true));
                } else if (name.equals("active_buy")) {
                    bd.and(aftersale.active_buyer.eq(true));
                } else if (name.equals("cancel")) {
                    bd.and(aftersale.cancel.eq(false));
                }
            }
        }
        return afterSaleRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));
    }

    public List<AfterSale> findByOrderId(int oid) {
        return afterSaleRepo.findByOrderId(oid);
    }

    public List<Message> messageList(int id){
       return messageService.findByAfterSaleId(id);
    }

    // 定时器1 买家申请后，卖家7日内未处理
    public void flush1() {
        List<AfterSale> list = afterSaleRepo.findByIsAgree("0");
        Date date = new Date();
        if (list != null && list.size() != 0) {
            for (AfterSale afterSale : list) {
                // 7 天
                if (date.getTime() - afterSale.getCreateDate().getTime() >= 3600 * 24 * 7 * 1000) {
                    //退款
                    //拼接参数
                    String param = "id=" + afterSale.getId() + "&sellerId=" + afterSale.getSellerId() + "&=isAgree=yes&response=系统默认同意";
                    //处理请求
                    String result = HttpRequest.sendGet("https://www.cslapp.com/after-sale/deal", param);
                    System.out.println("系统自动处理7日内商家未处理的退款、退货申请：" + result);
                }
            }
        }
    }

    // 定时器2 买家7日内未处理,关闭申请
    public void flush2() {
        // 填写物流
        List<AfterSale> list = afterSaleRepo.findByPages("4");
        // 拒绝退款
        list.addAll(afterSaleRepo.findByPages("3"));
        // 拒绝退货退款
        list.addAll(afterSaleRepo.findByPages("6"));
        Date date = new Date();
        if (list != null && list.size() != 0) {
            for (AfterSale afterSale : list) {
                // 最后发货日期  拒绝后七日内无操作
                if (date.after(afterSale.getEndDeliverDate()) || date.getTime() - afterSale.getDealDate().getTime() >= 3600 * 24 * 7 * 1000) {
                    afterSale.setClosed(true);
                    update(afterSale, null);
                }
            }
        }
    }

    //定时器3 10天内未确认收货，自动收货且退款
    public void flush3(){
        List<AfterSale> list = afterSaleRepo.findByPages("5");
        Date date = new Date();
        if (list != null && list.size() != 0) {
            for (AfterSale afterSale : list) {
                // 最后收货日期
                if (date.after(afterSale.getEndReceiveDate())) {
                    //退款
                    //拼接参数
                    String param = "id="+afterSale.getId();
                    //处理请求
                    String result = HttpRequest.sendGet("https://www.cslapp.com/after-sale/receive", param);
                    System.out.println("系统自动处理10日内商家没有确认收货：" + result);
                }
            }
        }
    }

}
