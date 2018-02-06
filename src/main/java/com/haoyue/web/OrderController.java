package com.haoyue.web;


import com.aliyuncs.exceptions.ClientException;
import com.haoyue.pojo.*;
import com.haoyue.pojo.Dictionary;
import com.haoyue.service.*;
import com.haoyue.untils.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;
import java.util.Collections;

/**
 * Created by LiJia on 2017/8/24.
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProdutsTypeService produtsTypeService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private DelievrService delievrService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CashTicketService cashTicketService;
    @Autowired
    private LuckDrawService luckDrawService;
    @Autowired
    private SellerService sellerService;


    // /order/list?pageNumber&pageSize&sellerId&state&active=true
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return new Result(false, Global.do_success, orderService.list(map, pageNumber, pageSize), map.get("token"));
    }


    @RequestMapping("/del")
    public Result del(Integer id, String openId) {
        Order order = orderService.findOne(id);
        Integer sellerId = order.getSellerId();
        Customer customer = customerService.findByOpenId(openId, sellerId + "");
        if (order.getCustomerId() != customer.getId()) {
            return new Result(true, Global.have_no_right, openId);
        }
        orderService.del(id);
        return new Result(false, Global.do_success, openId);
    }


    //  删除已完成订单 ，取消待付款订单,删除待评价订单  /order/cancel?id=订单ID&openId=12&sellerId=卖家ID
    @RequestMapping("/cancel")
    public Result cancel(Integer id, String openId, String sellerId) {
        Order order = orderService.findOne(id);
        if (order.getSellerId() != Integer.parseInt(sellerId)) {
            return new Result(true, Global.have_no_right, null);
        }
        //买家取消订单 ，买家看不到，卖家看得到
        if (!StringUtils.isNullOrBlank(openId)) {
            order.setActive(false);
        }
        //卖家取消订单，买卖双方都看不到
        if (StringUtils.isNullOrBlank(openId)) {
            order.setActive(false);
            order.setActive_seller(false);
        }
        orderService.update(order);
        return new Result(false, Global.do_success, null, null);
    }


    // /order/clist?sellerId&openId&state&active=true
    //  /order/clist?sellerId=122&openId=1221
    @RequestMapping("/clist")
    public Result clist(@RequestParam Map<String, String> map) {
        String openId = map.get("openId");
        String sellerId = map.get("sellerId");
        Customer customer = customerService.findByOpenId(openId, sellerId);
        map.put("cid", customer.getId() + "");
        return new Result(false, "", orderService.clist(map), map.get("token"));
    }

    //   /order/findOne?oid=订单Id&openId=2221
    @RequestMapping("/findOne")
    public Result findOne(Integer oid,String openId) {
        Order order = orderService.findOne(oid);
        return new Result(false, Global.do_success, order, null);
    }


    @RequestMapping("/save")
    public Result save(String deliver_price, Integer proId, Integer proTypeId, String sellerId, String receiver, String phone, String address, Integer amount, String openId, String leaveMessage, String usevip, String wxname, String cashTicketCode) {
        //  当用户点击拒接获取信息后，导致wxname,wxpic为空
        if (StringUtils.isNullOrBlank(wxname)){
            return new Result(true, Global.cannot_get_info, null, null);
        }
        //  当用户点击拒接获取信息后，导致openId为空
        if (StringUtils.isNullOrBlank(openId) || openId.equals("undefined")) {
            return new Result(true, Global.cannot_get_info, null, null);
        }
        Customer customer = customerService.findByOpenId(openId, sellerId);
        Order order = new Order();
        //客户
        order.setCustomerId(customer.getId());
        //微信昵称
        order.setWxname(wxname);
        //卖家留言
        order.setLeaveMessage_seller("");
        //商品
        Products products = productsService.findOne(proId);
        List<Products> productses = new ArrayList<>();
        productses.add(products);
        order.setProducts(productses);
        //商品分类
        ProdutsType produtsType = produtsTypeService.findOne(proTypeId);
        //判断库存量是否足够
        if (produtsType.getAmount() < amount) {
            return new Result(true, Global.amount_notEnough, null, null);
        }
        List<ProdutsType> produtsTypes = new ArrayList<>();
        produtsTypes.add(produtsType);
        order.setProdutsTypes(produtsTypes);
        //快递
        Deliver deliver = new Deliver();
        // if...else... 兼容新旧方法
        if (!StringUtils.isNullOrBlank(deliver_price)) {
            //根据商品快递模板和买家的地区，筛选出快递费用，并以 deliver_price 传递后后台
            deliver.setPrice(Double.valueOf(deliver_price));
        } else {
            deliver.setPrice(Double.valueOf(products.getDeliverPrice()) + 0);
        }
        delievrService.save2(deliver);
        order.setDeliver(deliver);
        //买家留言
        order.setLeaveMessage(leaveMessage);
        //地址
        Address address1 = new Address();
        address1.setOpenId(openId);
        address1.setAddress(address);
        address1.setPhone(phone);
        address1.setReceiver(receiver);
        addressService.save2(address1);
        order.setAddress(address1);
        //日期
        order.setCreateDate(new Date());
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,3);
        order.setLatestDate(calendar.getTime());
        //数量
        order.setAmount(amount);
        //发票
        order.setInvoiceType(Global.invoice_type);
        //支付方式
        order.setPayType(Global.pay_type);
        //原价
        order.setOldPrice(produtsType.getPriceOld() == null ? produtsType.getPriceNew() : produtsType.getPriceOld());
        //现价
        order.setPrice(produtsType.getDiscountPrice() * amount);
        //订单号 唯一
        synchronized (Global.object) {
            order.setOrderCode(Global.order_code_begin + (Global.count++) + new Date().getTime());
            if (Global.count >= 5000) {
                Global.count = 1;
            }
        }
        //卖家
        order.setSellerId(products.getSellerId());
        //新建订单为未付款订单
        order.setState(Global.order_unpay);
        //总计
        order.setTotalPrice(order.getPrice() + order.getDeliver().getPrice());
        //是否使用会员卡
        if (!StringUtils.isNullOrBlank(usevip) && usevip.equals("yes")) {
            Member member = memberService.findByOpenIdAndSellerId(openId, sellerId);
            order.setTotalPrice(order.getTotalPrice() * Double.valueOf(member.getDiscount()));
        }
        //是否使用抵用券
        if (!StringUtils.isNullOrBlank(cashTicketCode)) {
            CashTicket cashTicket = cashTicketService.findByCode(cashTicketCode);
            order.setTotalPrice(order.getTotalPrice() - Double.valueOf(cashTicket.getCash()));
            cashTicket.setIsuse(true);
            cashTicketService.update(cashTicket);
        }
        //是否抽奖订单
        if (products.getIsLuckDraw()) {
            if (products.getIsLuckDrawEnd()) {
                return new Result(true, Global.luckdraw_end_ornotbegin, null);
            }
            order.setState(Global.order_luckdraw_unpay);
            order.setIsLuckDraw(true);
            //判断抽奖人数是否满
            LuckDraw luckDraw = luckDrawService.findBySellerId(sellerId);
            if (luckDraw.getJoinNumber() + 1 > luckDraw.getAllNumber()) {
                return new Result(true, Global.luckdraw_num_enough, null, null);
            }
            //判断用户是否参加过一次
            Customer customer1 = customerService.findByOpenId(openId, sellerId);
            String joiner = luckDraw.getJoiners();
            if (!StringUtils.isNullOrBlank(joiner)) {
                String[] joiners = joiner.split(",");
                for (String str : joiners) {
                    if (StringUtils.isNullOrBlank(str)) {
                        continue;
                    }
                    if (str.equals(String.valueOf(customer1.getId()))) {
                        return new Result(true, Global.access_in_again, null, null);
                    }
                }
            }
        }
        return new Result(false, Global.do_success, orderService.save(order), null);
    }

    @RequestMapping("/changeState")
    public Result changeState(Integer oid, String state) {
        synchronized (Global.object2) {
            Order order = orderService.findOne(oid);
            order.setState(state);
            // 未付款 - 未发货
            if (state.equals(Global.order_unsend) || state.equals(Global.order_luckdraw)) {
                //每次付款后更新当前买家的会员信息
                //saveMember(order);
                //付款日期
                order.setPayDate(new Date());
                //更新 dictionary  全部 交易额 订单数量
                Dictionary dictionary = dictionaryService.findByDateAndSellerId(new Date(), order.getSellerId());
                dictionary.setBuyers(dictionary.getBuyers() + 1);
                dictionary.setTurnover(order.getTotalPrice() + dictionary.getTurnover());
                dictionaryService.update(dictionary);
                //更新商品已经卖出量
                List<Products> list = order.getProducts();
                List<ProdutsType> produtsTypes = order.getProdutsTypes();
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        Products products = list.get(i);
                        products.setMonthSale(products.getMonthSale() + order.getAmount());
                        // 更新 单个商品的交易额 订单数
                        Dictionary dictionary1 = dictionaryService.findByProductId(products.getId());
                        dictionary1.setBuyers(dictionary1.getBuyers() + 1);
                        dictionary1.setTurnover(dictionary1.getTurnover() + order.getTotalPrice());
                        dictionaryService.update(dictionary1);
                    }
                }
                //更新商品分类存库量
                for (ProdutsType produtsType : produtsTypes) {
                    produtsType.setAmount(produtsType.getAmount() - order.getAmount());
                }
                productsService.updateList(list);
                produtsTypeService.update(produtsTypes);
            }
            //如果是抽奖订单
            if (state.equals(Global.order_luckdraw)) {
                LuckDraw luckDraw = luckDrawService.findBySellerId(order.getSellerId() + "");
                //参与人数+1
                luckDraw.setJoinNumber(luckDraw.getJoinNumber() + 1);
                //产生随机中奖号码
                int allNumber = luckDraw.getAllNumber();
                int random = (int) Math.ceil(Math.random() * allNumber);
                String random_str = random + "";
                //找出所有已经下单的抽奖订单的抽奖号码
                List<String> luckcodes = orderService.findByLuckCodeBySeller(order.getSellerId());
                //判断该号码是否已存在
                while (luckcodes != null && luckcodes.contains(random_str)) {
                    random = (int) Math.ceil(Math.random() * allNumber);
                    random_str = random + "";
                }
                order.setLuckcode(String.valueOf(random));
                //判断是否中奖
                String luckNumbers[] = luckDraw.getLackNumber().split("=");
                for (String str : luckNumbers) {
                    if (StringUtils.isNullOrBlank(str)) {
                        continue;
                    }
                    // 中奖
                    if (String.valueOf(random).equals(str)) {
                        order.setIsLuck(true);
                    }
                }
                //判断抽奖活动是否结束
                if (luckcodes == null) {
                    luckcodes = new ArrayList<>();
                }
                orderService.update(order);
                //如果抽奖活动结束，将抽奖订单设置抽奖结束，中奖订单设置成待发货
                if (luckDraw.getJoinNumber() == allNumber) {
                    Products products = order.getProducts().get(0);
                    products.setIsLuckDrawEnd(true);
                    productsService.update(products);
                    orderService.updateIsLuckDrawEnd(products.getId());
                }
                //增加参与者
                luckDraw.setJoiners(luckDraw.getJoiners() + "," + order.getCustomerId());
                luckDrawService.update(luckDraw);
            }

            //微信付款通知模板
            if (!state.equals(Global.order_unpay) && !state.equals(Global.order_luckdraw_unpay)) {
                addTemplate(order);
            }
            orderService.update(order);
            //更新个人花费总额
            Customer customer = customerService.findOne(order.getCustomerId());
            customer.setExpense(customer.getExpense() + order.getTotalPrice());
            customerService.update(customer);

            return new Result(false, Global.do_success, order, null);
        }
    }

    // https://www.cslapp.com/order/excel?sellerId=3&oids=714=715
    // http://localhost:8080/order/excel?sellerId=1&state=已完成订单&openId=1
    @RequestMapping("/excel")
    public Result excel(String sellerId, String oids) throws IOException {
        return orderService.excel(sellerId, oids);
    }

    @RequestMapping("/update")
    public Result update(Order order, String selleId, String changePrice) {
        Order order1 = orderService.findOne(order.getId());
        //卖家备注
        if (StringUtils.isNullOrBlank(changePrice)) {
            order1.setLeaveMessage_seller(order.getLeaveMessage_seller());
        } else {
            //修改待付款订单总价
            if (!order1.getState().equals(Global.order_unpay)) {
                return new Result(true, Global.order_not_unpay, null, null);
            }
            order1.setTotalPrice(order.getTotalPrice());
        }
        orderService.update(order1);
        return new Result(false, Global.do_success, null, null);
    }

    // http://localhost:8080/order/remind_deliver?sellerId=1&oid=140
    // 提醒卖家发货
    @RequestMapping("/remind_deliver")
    public Result remindDeliver(String sellerId, String oid) {
        Order order = orderService.findOne(Integer.parseInt(oid));
        String wxname = order.getWxname();
        String code = order.getOrderCode();
        String phone = sellerService.findOne(Integer.parseInt(sellerId)).getSellerPhone();
        try {
            SendCode.sendSms2(phone, code, wxname);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new Result(false, Global.do_success, null, null);
    }


    //模糊查询 商品编号 订单编号 订单状态 买家名称 商品名称 买家昵称
    // http://localhost:8080/order/query?sellerId=卖家ID&ordercode=订单号&pcode=商品号&pname=商品名&state=订单状态&wxname=买家昵称
    @RequestMapping("/query")
    public Result query(@RequestParam Map<String, String> map) {
        Iterable<Order> iterable = orderService.filter(map);
        return new Result(false, Global.do_success, iterable, null);
    }

    //  http://localhost:8080/order/deliver?sellerId=1&code=3910771985669
    @RequestMapping("/deliver")
    public Result kuaidi(String sellerId,String code){
        String result=Kuaidi.getDetail(code);
        System.out.println(result);
        return new Result(false, Global.do_success, result, null);
    }


    //每当买家订单付款后会更新该买家的会员信息
    public void saveMember(Order order) {
        String cid = order.getCustomerId() + "";
        String open_id = customerService.findOpenIdById(cid);
        double total_price = orderService.getToTalPriceByCustomerId(cid, order.getSellerId() + "", orderService.getdate());
        List<Member> memberList = memberService.findBySellerIdAndOpenIdIsNull(order.getSellerId() + "");
        int leavel = 0;
        String lev_1 = "";
        String lev_2 = "";
        String lev_3 = "";
        for (Member member : memberList) {
            if (total_price >= Double.valueOf(member.getTotal_consume())) {
                leavel++;
            }
            if (member.getLeavel().equals("lev1")) {
                lev_1 = member.getDiscount();
            }
            if (member.getLeavel().equals("lev2")) {
                lev_2 = member.getDiscount();
            }
            if (member.getLeavel().equals("lev3")) {
                lev_3 = member.getDiscount();
            }
        }
        if (leavel != 0) {
            Member member = memberService.findByOpenIdAndSellerId(open_id, order.getSellerId() + "");
            // 区别被降级的会员和升级的会员
            if (total_price > Double.valueOf(member.getTotal_consume())) {
                if (member == null && member.getId() == null) {
                    member = new Member();
                    member.setOpenId(open_id);
                    member.setSellerId(order.getSellerId() + "");
                    member.setCreateDate(new Date());
                }
                member.setTotal_consume(String.valueOf(total_price));
                member.setLeavel("lev" + leavel);
                if (leavel == 1) {
                    member.setDiscount(lev_1);
                }
                if (leavel == 2) {
                    member.setDiscount(lev_2);
                }
                if (leavel == 3) {
                    member.setDiscount(lev_3);
                }
                member.setWxname(order.getWxname());
                memberService.save(member);
                if (StringUtils.isNullOrBlank(member.getCode())) {
                    member.setCode((8888 + member.getId()) + "");
                    memberService.save(member);
                }
            }
        }
    }

    public void getTemplate(Template template, int sellerId,int oid) {
        //获取 appId 和 secret 8bcdb74a9915b5685fa0ec37f6f25b24
        Seller seller = sellerService.findOne(sellerId);
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=" + seller.getAppId() + "&secret=" + seller.getSecret();
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        //发送模板信息
        String form_id = Global.package_map.get(oid+"");
        template.setForm_id(form_id);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + form_id;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("付款通知：：："+result);
        //刷新 Global.package_map
        Global.package_map.remove(oid+"");
    }

    public void addTemplate(Order order) {

        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(order.getProducts().get(0).getPname());
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(order.getWxname());
        list.add(templateResponse2);

        TemplateResponse templateResponse3 = new TemplateResponse();
        templateResponse3.setColor("#000000");
        templateResponse3.setName("keyword3");
        templateResponse3.setValue(order.getTotalPrice() + "");
        list.add(templateResponse3);

        TemplateResponse templateResponse4 = new TemplateResponse();
        templateResponse4.setColor("#000000");
        templateResponse4.setName("keyword4");
        templateResponse4.setValue("微信支付");
        list.add(templateResponse4);

        TemplateResponse templateResponse5 = new TemplateResponse();
        templateResponse5.setColor("#000000");
        templateResponse5.setName("keyword5");
        String date = StringUtils.formDateToStr(new Date());
        templateResponse5.setValue(date);
        list.add(templateResponse5);

        Template template = new Template();
        template.setTemplateId(sellerService.findOne(order.getSellerId()).getPaysuccess_template());
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage("pages/index/index");
        template.setToUser(customerService.findOpenIdById(order.getCustomerId() + ""));
        getTemplate(template, order.getSellerId(),order.getId());
    }

}
