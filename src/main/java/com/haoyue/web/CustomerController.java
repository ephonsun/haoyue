package com.haoyue.web;

import com.haoyue.pojo.Customer;
import com.haoyue.pojo.IntegralRecord;
import com.haoyue.pojo.Member;
import com.haoyue.pojo.Seller;
import com.haoyue.service.*;
import com.haoyue.untils.*;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

/**
 * Created by LiJia on 2017/9/4.
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ProductsService productsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private IntegralRecordService integralRecordService;


    //关键词查询 商品分类查询
    @RequestMapping("/proSearch")
    public Result proSearch(@RequestParam Map<String, String> map) {
        // map.put("showdate","yes");
        return new Result(false, Global.do_success, productsService.list(map), map.get("token"));
    }

    // 客户访问店铺的时候
    // https://www.cslapp.com/customer/loginOrReg?openId=oARLy0PHaLUP-jeCSyzJXh8-QV-A&sellerId=5&wxname=微信名称&wxpic=微信头像
    @RequestMapping("/loginOrReg")
    public Result save(String openId, String sellerId, String wxname, String wxpic) {

        // boolean flag=sellerService.isStop(sellerId);
        Seller seller = sellerService.findOne(Integer.parseInt(sellerId));
        boolean flag = seller.getIsActive();
        boolean isCoupon = seller.getIscoupon();
        //判断该店铺服务是否停止
        if (!flag) {
            return new Result(true, Global.service_stop, null, null);
        }
        //优惠券活动是否开启
        if (isCoupon) {
            return new Result(true, Global.coupon_isopen, null, null);
        }
        if (openId.equals("undefined")) {
            return new Result(true, Global.openId_undefined, null, null);
        }
        //判断用户是否存在
        Customer customer = customerService.findByOpenId(openId, sellerId);
        if (customer == null) {
            //不存在
            List<Customer> customerList = customerService.findByWxnamAndSellerId(wxname, sellerId);
            //之前没有登录过微商城
            if (customerList == null || customerList.size() == 0) {
                customer = new Customer();
                customer.setOpenId(openId);
                customer.setSellerId(sellerId);
                customer.setCreateDate(new Date());
                customer.setWxname(wxname);
                customer.setWxpic(wxpic);
                customer = customerService.save(customer);
            } else {
                //之前有登录过微商城
                if (customerList.size() == 1) {
                    //数据唯一
                    customer = customerList.get(0);
                    customer.setOpenId(openId);
                    customerService.save(customer);
                } else {
                    //数据不唯一
                    return new Result(false, Global.customer_not_only, customerList, null);
                }
            }
        } else {
            //存在
            customer.setWxname(wxname);
            customer.setWxpic(wxpic);
            customerService.update(customer);
            //更改订单中微信名和头像
            orderService.updateWxname(wxname, customer.getId());
            //更新评论中微信名和头像
            commentService.updateWxname(wxname, wxpic, customer.getOpenId(), customer.getSellerId());

        }
        return new Result(false, Global.do_success, customer, null);
    }

    //  /customer/web-login?wxname=微信昵称&webOpenId=openId&sex=性别&province=省份&city=城市&wxpic=头像
    @RequestMapping("/web-login")
    public Result weblogin(Customer customer) {
        //检验微信名
        if (StringUtils.isNullOrBlank(customer.getWxname())) {
            return new Result(true, Global.wxname_isNullorBlank, null, null);
        }
        //首先查找用户是否存在
        Customer oldone = customerService.findByWebOpenIdAndSellerId(customer.getWebOpenId(), customer.getSellerId());
        if (oldone != null) {
            //存在
            return new Result(false, Global.do_success, oldone, null);
        } else {
            //不存在
            List<Customer> customerList = customerService.findByWxnamAndSellerId(customer.getWxname(), customer.getSellerId());
            //在此之前没有访问过小程序
            if (customerList == null) {
                customer.setCreateDate(new Date());
                customer.setOpenId(customer.getWebOpenId());
                customerService.save(customer);
            }
            //在此之前有访问过小程序
            if (customerList != null && customerList.size() != 0) {
                //判断查询记录是否唯一
                if (customerList.size() == 1) {
                    oldone = customerList.get(0);
                    oldone.setWebOpenId(customer.getWebOpenId());
                    customerService.save(oldone);
                    customer = oldone;
                } else {
                    //查询记录不唯一,返回多个用户供选择
                    return new Result(false, Global.customer_not_only, customerList, null);
                }
            }
            return new Result(false, Global.do_success, customer, null);
        }
    }

    //  /customer/getSessionKey?appId=小程序ID&code=12&secret=小程序secret
    @RequestMapping("/getSessionKey")
    public Result getOpenId(String appId, String code, String secret) {
        String response = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", "appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code");
        System.out.println(response);
        return new Result(false, null, response, null);
    }

    // https://www.cslapp.com/customer/getAccess_token?code=code&sellerId=3
    @RequestMapping("/getAccess_token")
    public Result getAccess_token(String code) {
        String response = HttpRequest.sendGet(" https://api.weixin.qq.com/sns/oauth2/access_token", "appid=wxae8906e8139f3fe8&secret=34694bb20f76f2ebce095c845ec3919e&code=" + code + "&grant_type=authorization_code");
        System.out.println(response);
        return new Result(false, null, response, null);
    }

    // https://www.cslapp.com/customer/getUser_info?code=code&sellerId=3
    @RequestMapping("/getUser_info")
    public Result getUser_info(String code) {
        String response1 = HttpRequest.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", "appid=wxae8906e8139f3fe8&secret=34694bb20f76f2ebce095c845ec3919e&code=" + code + "&grant_type=authorization_code");
        JSONObject jsonObject = net.sf.json.JSONObject.fromObject(response1);
        String access_token = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");
        String response = HttpRequest.sendGet("https://api.weixin.qq.com/sns/userinfo", "access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN");
        return new Result(false, null, response, null);
    }

    @RequestMapping("/getPhone")
    public Result getPhone(String encryptedData, String iv, String session_key) {
        WXAppletUserInfo wxAppletUserInfo = new WXAppletUserInfo();
        String response = wxAppletUserInfo.decodeUserInfo(encryptedData, iv, session_key);
        System.out.println("response===" + response);
        return new Result(false, Global.do_success, response, null);
    }


    // /customer/update_integral?openId=123&sellerId=123&nums=使用积分数
    @RequestMapping("/update_integral")
    public Result updateIntegral(String openId, String sellerId, String nums) {
        Customer customer = customerService.findByOpenId(openId, sellerId);
        if (Integer.parseInt(nums) > customer.getUnuseScroll()) {
            return new Result(true, Global.data_unright + ":" + nums, null, null);
        }
        customer.setUnuseScroll(customer.getUnuseScroll() - Integer.parseInt(nums));
        customer.setUsedScroll(customer.getUsedScroll() + Integer.parseInt(nums));
        customerService.update(customer);
        //增加积分的记录
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setCreateDate(new Date());
        integralRecord.setOpenId(customer.getOpenId());
        integralRecord.setSellerId(customer.getSellerId());
        integralRecord.setScrolls(Integer.parseInt(nums));
        integralRecord.setType("1");//消费积分
        integralRecordService.save(integralRecord);
        return new Result(false, Global.do_success, null, null);
    }

    // /customer/update?openId=1213&sellerId=3&sex=性别&phone=手机号&email=邮箱&birthday=生日(例如 10-01)
    // &province=省份&city=城市名称(调用高德地图api)
    // http://lbs.amap.com/api/javascript-api/guide/map-data/geocoding
    @RequestMapping("/update")
    public Result update(Customer customer) {

        //校验一下手机号码是否已经被使用
        Customer oldcustomer = customerService.findByPhone(customer.getPhone(),customer.getSellerId());
        if (oldcustomer != null&&!customer.getOpenId().equals(oldcustomer.getOpenId())) {
            if(oldcustomer.getSellerId().equals(customer.getSellerId())){
                return new Result(true, Global.phone_exist, null, null);
            }
        }

        Customer customer1 = customerService.findByOpenId(customer.getOpenId(), customer.getSellerId());

        customer1.setSex(customer.getSex());
        customer1.setPhone(customer.getPhone());
        customer1.setEmail(customer.getEmail());
        customer1.setBirthdays(customer.getBirthday());
        customer1.setBirthday(customer.getBirthday().substring(customer.getBirthday().indexOf("-") + 1));
        customer1.setProvince(customer.getProvince());
        customer1.setCity(customer.getCity());
        customerService.update(customer1);

        //更新会员信息
        Member member = memberService.findByOpenIdAndSellerId(customer.getOpenId(), customer.getSellerId());
        if (member != null) {
            member.setPhone(customer.getPhone());
            member.setSex(customer.getSex());
            member.setBirthday(customer.getBirthday());
            member.setProvince(customer.getProvince());
            member.setCity(customer.getCity());
            try {
                member.setBirthDate(StringUtils.formatDate2(Calendar.getInstance().get(Calendar.YEAR) + "-" + customer.getBirthday() + " 00:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            memberService.save(member);
        }

        return new Result(false, Global.do_success, customer1, null);
    }

    // /customer/scroll_list?sellerId=3&havescroll=3
    @RequestMapping("/scroll_list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<Customer> iterable = customerService.list2(map, pageNumber, pageSize);
        Iterator<Customer> iterator = iterable.iterator();
        List<Customer> customerList = new ArrayList<>();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (customer.getSellerId().equals(map.get("sellerId"))) {
                customerList.add(customer);
            }
        }
        return new Result(false, Global.do_success, customerList, null);
    }


    //   更新账户充值余额
    //  /customer/recharge?openId=123&sellerId=112&moneys=消费金额(元)
    @RequestMapping("/recharge")
    public Result recharge(String openId,String sellerId,double moneys){

        Customer customer=customerService.findByOpenId(openId,sellerId);
        double rechargemoney= customer.getRechargeMoney();
        if(rechargemoney<moneys){
            return new Result(true, Global.recharge_money_less, null, null);
        }
        customer.setRechargeMoney(customer.getRechargeMoney()-moneys);
        customerService.update(customer);
        return new Result(false, Global.do_success, null, null);

    }

}
