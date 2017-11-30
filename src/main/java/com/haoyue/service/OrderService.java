package com.haoyue.service;

import com.haoyue.pojo.Member;
import com.haoyue.pojo.Order;
import com.haoyue.pojo.OrderTotalPrice;
import com.haoyue.pojo.QOrder;
import com.haoyue.repo.OrderRepo;
import com.haoyue.untils.Global;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/8/24.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MemberService memberService;

    public Iterable<Order> list(Map<String, String> map, int pageNumber, int pageSize) {

        QOrder order = QOrder.order;
        BooleanBuilder bd = new BooleanBuilder();
        boolean flag = false;
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("id")) {
                    bd.and(order.id.eq(Integer.parseInt(value)));
                }
                if (name.equals("token")) {
                    Date now = new Date();
                    Date befor = new Date();
                    befor.setDate(1);
                    befor.setHours(0);
                    befor.setMinutes(0);
                    befor.setSeconds(0);
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                    bd.and(order.createDate.between(befor, now));
                    bd.and(order.state.notIn(Global.order_unpay));
                }
                if (name.equals("state")) {
                    bd.and(order.state.eq(value));
                }
                if (name.equals("sellerId")) {
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("isApplyReturn")) {
                    bd.and(order.isApplyReturn.eq(Boolean.valueOf(value)));
                }
                if (name.equals("luckdraw")) {
                    bd.and(order.isLuckDraw.eq(true));
                    bd.and(order.state.ne(Global.order_luckdraw_unpay));
                    flag = true;
                }
                if (name.equals("active")){
                    bd.and(order.active.eq(Boolean.valueOf(value)));
                }
                if (name.equals("luck")) {
                    bd.and(order.isLuck.eq(true));
                }
            }
        }
        if (flag) {
            int count = orderRepo.findLuckDrawSize(map.get("sellerId"));
            return orderRepo.findAll(bd.getValue(), new PageRequest(pageNumber, count, new Sort(Sort.Direction.DESC, new String[]{"createDate"})));
        }
        return orderRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, new String[]{"createDate"})));
    }

    public Iterable<Order> clist(Map<String, String> map) {

        QOrder order = QOrder.order;
        BooleanBuilder bd = new BooleanBuilder();

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("cid")) {
                    bd.and(order.customerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("state")) {
                    if (!value.equals("全部订单")) {
                        bd.and(order.state.contains(value));
                    }
                }
                if (name.equals("sellerId")) {
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("active")) {
                    bd.and(order.active.eq(Boolean.valueOf(value)));
                }
                if (name.equals("luckdraw")) {
                    bd.and(order.isLuckDraw.eq(true));
                }

            }
        }
        return orderRepo.findAll(bd.getValue());
    }

    public Order findOne(Integer id) {
        return orderRepo.findOne(id);
    }

    public void del(Integer id) {
        orderRepo.delete(id);
    }

    public void update(Order order) {
        orderRepo.save(order);
    }

    public Order save(Order order) {
        return orderRepo.save(order);
    }

    public List<Order> findUnDone(String order_send) {
        return orderRepo.findByState(order_send);
    }


    public double getToTalPriceByCustomerId(String cid, String sellerId, Date date) {
        return orderRepo.getToTalPriceBySellerId(cid, sellerId, date);
    }

    public Date getdate() {
        Date date = new Date();
        date.setMonth(0);
        date.setDate(1);
        date.setHours(0);
        date.setMinutes(0);
        return date;
    }

    // 卖家更新会员系统后，刷新所有买家会员信息
    public void getToTalPriceByCustomer(String sellerId, List<Member> news) {
        // 查询该年至今所有买家的交易额
        List<OrderTotalPrice> list = orderRepo.getToTalPriceBySellerAndCustomer(sellerId, getdate());
        String total_consume_1 = "";
        String discount_1 = "";
        String total_consume_2 = "";
        String discount_2 = "";
        String total_consume_3 = "";
        String discount_3 = "";
        int index = 0;
        int customer_id = 0;
        String openId = "";
        String result_discount = "";
        for (Member m : news) {
            if (m.getLeavel().equals("lev1")) {
                total_consume_1 = m.getTotal_consume();
                discount_1 = m.getDiscount();
            }
            if (m.getLeavel().equals("lev2")) {
                total_consume_2 = m.getTotal_consume();
                discount_2 = m.getDiscount();
            }
            if (m.getLeavel().equals("lev3")) {
                total_consume_3 = m.getTotal_consume();
                discount_3 = m.getDiscount();
            }
        }
        // 遍历每一个买家的交易额和会员成长体系进行对比
        for (OrderTotalPrice each : list) {
            if (each.getTotal_price() >= Double.valueOf(total_consume_1)) {
                index = 1;
                result_discount = discount_1;
            }
            if (each.getTotal_price() >= Double.valueOf(total_consume_2)) {
                index = 2;
                result_discount = discount_2;
            }
            if (each.getTotal_price() >= Double.valueOf(total_consume_3)) {
                index = 3;
                result_discount = discount_3;
            }
            // 新增会员，更新老会员
            if (index != 0) {
                customer_id = each.getCustomerId();
                openId = customerService.findOpenIdById(customer_id + "");
                Member member = memberService.findByOpenIdAndSellerId(sellerId, openId);
                if (member == null && member.getId() == null) {
                    member = new Member();
                    member.setSellerId(sellerId);
                    member.setCreateDate(new Date());
                    member.setOpenId(openId);
                    member.setTotal_consume(each.getTotal_price() + "");
                }
                member.setDiscount(result_discount);
                member.setLeavel("lev" + index);
                memberService.save(member);
                if (StringUtils.isNullOrBlank(member.getCode())) {
                    member.setCode((8888 + member.getId()) + "");
                    memberService.save(member);
                }
            }
            //删除老会员
            if (index == 0) {
                Member member = memberService.findByOpenIdAndSellerId(sellerId, openId);
                if (member != null && member.getId() != null) {
                    memberService.del(member);
                }
            }
        }
    }

    public List<String> findBySellerIdAndProIdAndIsLuckDrawEnd(Integer sellerId, Integer pid) {
        List<Integer> oids = orderRepo.findBySellerIdAndProIdAndIsLuckDrawEnd(pid);
        List<String> codes = new ArrayList<>();
        for (Integer id : oids) {
            Order order = orderRepo.findOne(id);
            if (order.getIsLuckDraw() == true && order.getIsLuckDrawEnd() == false) {
                codes.add(order.getLuckcode());
            }
        }
        return codes;
    }

    public void updateIsLuckDrawEnd(Integer pid) {
        List<Integer> oids = orderRepo.findBySellerIdAndProIdAndIsLuckDrawEnd(pid);
        for (Integer id : oids) {
            Order order = orderRepo.findOne(id);
            if (order.getIsLuckDrawEnd() == false) {
                order.setIsLuckDrawEnd(true);
                if (order.getIsLuck()) {
                    //中奖-待发货
                    order.setState(Global.order_unsend);
                } else {
                    //未中奖-已完成
                    order.setState(Global.order_finsh);
                }
                orderRepo.save(order);
            }
        }
    }

    public List<Order> findBySellerIdAndCreateDate(String sellerId, Date from, Date end) {
        return orderRepo.findBySellerIdAndCreateDate(sellerId, from, end);
    }

    public void updateIsLuckDrawEndBySeller(String sellerId) {
        orderRepo.updateIsLuckDrawEndBySeller(sellerId);
    }

    public List<String> findByLuckCodeBySeller(Integer sellerId) {
        return orderRepo.findByLuckCodeBySeller(sellerId);
    }
}
