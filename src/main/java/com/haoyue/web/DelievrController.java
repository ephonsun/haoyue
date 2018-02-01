package com.haoyue.web;

import com.haoyue.pojo.AfterSale;
import com.haoyue.pojo.Deliver;
import com.haoyue.pojo.Order;
import com.haoyue.service.AfterSaleService;
import com.haoyue.service.DelievrService;
import com.haoyue.service.OrderService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Kuaidi;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/24.
 */
@RestController
@RequestMapping("/deliver")
public class DelievrController {

    @Autowired
    private DelievrService delievrService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AfterSaleService afterSaleService;

    @RequestMapping("/save")
    public Result deliver(Integer oid, Deliver deliver, String token, String iscontinue) {
        Order order = orderService.findOne(oid);

        if (order.getSellerId() != Integer.parseInt(token)) {
            return new Result(true, Global.have_no_right, null, token);
        }
        if (StringUtils.isNullOrBlank(deliver.getDname()) || StringUtils.isNullOrBlank(deliver.getDcode())) {
            return new Result(true, Global.data_unright, null, null);
        }
        //判断快递单号-快递名是否存在
        List<Deliver> deliver2 = delievrService.findByDcodeAndDename(deliver.getDcode(), deliver.getDename());
        if (deliver2 != null&&deliver2.size()!=0) {
            if (StringUtils.isNullOrBlank(iscontinue)) {
                return new Result(true, Global.record_exist, null, null);
            }
        }
        Deliver deliver1 = order.getDeliver();
        deliver.setId(deliver1.getId());
        deliver.setCreateDate(new Date());//发送日期
        delievrService.update(deliver);
        order.setDeliver(deliver);
        order.setState(Global.order_send);

        //未发货订单，买家申请退款，买卖双方协商后，卖家发货，覆盖退款申请
        if (order.getIsApplyReturn()) {
            order.setIsApplyReturn(false);
            List<AfterSale> list = afterSaleService.findByOrderId(oid);
            if (list != null && list.size() != 0) {
                //删除原退款申请
                afterSaleService.del(list.get(0).getId());
            }
        }

        orderService.update(order);
        return new Result(false, Global.do_success, order, token);
    }

    @Transactional
    @RequestMapping("/save_template")
    public Result seller_template(String delivers, Deliver deliver) {
        //判断新建的物流模板是不是已经存在
        List<String> dnames = delievrService.findDnamesBySellerId(deliver.getSellerId());
        if (dnames != null && dnames.size() != 0) {
            for (String str : dnames) {
                if (str.contains(deliver.getDname()) || deliver.getDname().contains(str)) {
                    return new Result(true, Global.template_exist, null, null);
                }
            }
        }
        //顺丰快递的计价方式为重量计费
        if (deliver.getDname().contains("顺丰")) {
            if (!deliver.getPrice_type().equals("重量")) {
                return new Result(true, Global.weight_required, null, null);
            }
        }
        String lines[] = null;
        lines = delivers.split("-");

        String line[] = null;
        String destination = null;
        double account = deliver.getAccount();
        double price = deliver.getPrice();
        double more_account = deliver.getMore_account();
        double more_price = deliver.getMore_price();
        //默认运费，收货地区找不到买家地址
        String dname = deliver.getDname();
        Deliver deliver1 = new Deliver();
        deliver1.setSellerId(deliver.getSellerId());
        deliver1.setOrigin_address(deliver.getOrigin_address() + "");
        deliver1.setDname(deliver.getDname());
        deliver1.setPrice_type(deliver.getPrice_type());
        deliver1.setPrice(price);
        deliver1.setAccount(account);
        deliver1.setMore_account(more_account);
        deliver1.setMore_price(more_price);
        delievrService.save2(deliver1);

        //快递模板信息保存
        for (int i = 0; i < lines.length; i++) {
            Deliver newdeliver = new Deliver();
            line = lines[i].split("#");

            if (!StringUtils.isDiget(line[1])) {
                return new Result(true, Global.account_unright, null, null);
            }
            if (!StringUtils.isDiget(line[2])) {
                return new Result(true, Global.price_unright, null, null);
            }
            if (!StringUtils.isDiget(line[3])) {
                return new Result(true, Global.more_account_unright, null, null);
            }
            if (!StringUtils.isDiget(line[4])) {
                return new Result(true, Global.more_price_unright, null, null);
            }

            //dname = line[0];
            destination = line[0];
            account = Double.parseDouble(line[1]);
            price = Double.parseDouble(line[2]);
            more_account = Double.parseDouble(line[3]);
            more_price = Double.valueOf(line[4]);

            newdeliver.setOrigin_address(deliver.getOrigin_address());
            newdeliver.setPrice_type(deliver.getPrice_type());
            newdeliver.setSellerId(deliver.getSellerId());
            newdeliver.setDname(dname);
            newdeliver.setDestination(destination);
            newdeliver.setAccount(account);
            newdeliver.setPrice(price);
            newdeliver.setMore_account(more_account);
            newdeliver.setMore_price(more_price);
            delievrService.save2(newdeliver);
        }

        return new Result(false, Global.do_success, null, null);
    }


    @RequestMapping("/getTemplate")
    public Result getTamplate(String sellerId, String dname) {
        if (StringUtils.isNullOrBlank(dname)) {
            List deliverList = delievrService.findBySellerId(sellerId);
            return new Result(false, Global.do_success, deliverList, null);
        } else {
            List<Deliver> list = delievrService.findBySellerIdAndDname(sellerId, dname);
            if (list == null || list.size() == 0) {
                return new Result(true, Global.record_unexist, null, null);
            }
            return new Result(false, Global.do_success, list, null);
        }
    }

    @RequestMapping("/del")
    public Result del(String id, String sellerId, String dname) {

        if (StringUtils.isNullOrBlank(id)) {

        }

        if (StringUtils.isNullOrBlank(dname)) {
            return delievrService.delLine(id, sellerId);
        } else {
            delievrService.deleteByDnameAndSellerId(dname, sellerId);
            return new Result(false, Global.do_success, null, null);
        }
    }

    @RequestMapping("/update")
    public Result update(Deliver deliver, String sellerId) {
        //添加一行数据
        if (deliver.getId() == null) {
            if (!StringUtils.isDiget(deliver.getAccount() + "")) {
                return new Result(true, Global.account_unright, null, null);
            }
            if (!StringUtils.isDiget(deliver.getPrice() + "")) {
                return new Result(true, Global.price_unright, null, null);
            }
            if (!StringUtils.isDiget(deliver.getMore_account() + "")) {
                return new Result(true, Global.more_account_unright, null, null);
            }
            if (!StringUtils.isDiget(deliver.getMore_price() + "")) {
                return new Result(true, Global.more_price_unright, null, null);
            }
            deliver.setSellerId(sellerId);
            delievrService.save2(deliver);
            return new Result(false, Global.do_success, null, null);

        }
        //更新一行数据
        else {
            Deliver deliver1 = delievrService.findOne(deliver.getId());
            //检验数据格式
            if (!StringUtils.isDiget(deliver.getAccount() + "")) {
                return new Result(true, Global.account_unright, null, null);
            }
            if (!StringUtils.isDiget(String.valueOf(deliver.getPrice()))) {
                return new Result(true, Global.price_unright, null, null);
            }
            if (!StringUtils.isDiget(deliver.getMore_account() + "")) {
                return new Result(true, Global.more_account_unright, null, null);
            }
            if (!StringUtils.isDiget(String.valueOf(deliver.getMore_price()))) {
                return new Result(true, Global.more_price_unright, null, null);
            }
            //更新
            deliver1.setDestination(deliver.getDestination());
            deliver1.setAccount(deliver.getAccount());
            deliver1.setPrice(deliver.getPrice());
            deliver1.setMore_account(deliver.getMore_account());
            deliver1.setMore_price(deliver.getMore_price());
            delievrService.save2(deliver1);
            return new Result(false, Global.do_success, null, null);
        }
    }
}
