package com.haoyue.web;

import com.aliyuncs.exceptions.ClientException;
import com.haoyue.pojo.*;
import com.haoyue.service.*;
import com.haoyue.untils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by LiJia on 2017/9/29.
 */
@RestController
@RequestMapping("/super-admin")
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopCarService shopCarService;
    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping("/login")
    public Result login(SuperAdmin superAdmin) {
        SuperAdmin superAdmin1 = superAdminService.login(superAdmin);
        if (superAdmin1 != null) {
            superAdmin1.setAdmin_pass("*******");
        }
        return new Result(false, Global.do_success, superAdmin1, null);
    }

    @RequestMapping("/update")
    public Result update_name(SuperAdmin superAdmin) {
        SuperAdmin superAdmin1 = superAdminService.findOne(superAdmin.getId());
        if (!StringUtils.isNullOrBlank(superAdmin.getAdmin_name())) {
            superAdmin1.setAdmin_name(superAdmin.getAdmin_name());
        }
        if (!StringUtils.isNullOrBlank(superAdmin.getAdmin_phone())) {
            superAdmin1.setAdmin_phone(superAdmin.getAdmin_phone());
        }
        if (!StringUtils.isNullOrBlank(superAdmin.getAdmin_pass())) {
            superAdmin1.setAdmin_pass(superAdmin.getAdmin_pass());
        }
        superAdminService.upadte(superAdmin1);
        return new Result(false, Global.do_success, null, null);
    }

    @RequestMapping("/check_pass")
    public Result checkOldPass(String id, String oldPass) {
        SuperAdmin superAdmin = superAdminService.findOne(Integer.parseInt(id));
        if (!superAdmin.getAdmin_pass().equals(oldPass)) {
            return new Result(true, Global.oldPass_unRigt, null, null);
        }
        return new Result(true, Global.oldPass_Rigt, null, null);
    }

    @RequestMapping("/get_phoneCode")
    public Result getPhoneCode(String id) {
        SuperAdmin superAdmin = superAdminService.findOne(Integer.parseInt(id));
        String code = StringUtils.getPhoneCode();
        try {
            SendCode.sendSms(superAdmin.getAdmin_phone(), code);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new Result(true, Global.do_success, code, null);
    }

    /**
     * 版本升级
     *
     * @param phone
     * @param toVersion
     * @return
     */
    @RequestMapping("/up")
    public Result up(String phone, String toVersion) {
        Seller seller = sellerService.findBySellerPhone(phone);
        seller.setAuthority(toVersion);
        int i = toVersion.equals("1") ? 5 : 10;
        seller.setMaxFileSize(Global.max_file_size * i);
        sellerService.update2(seller);
        return new Result(false, Global.do_success, null, null);
    }

    /**
     * 增加存储空间
     *
     * @param token
     * @param amount
     * @return
     */
    @RequestMapping("/add-upload")
    public Result addUpload(String token, String phone, String amount) {
        if (StringUtils.isNullOrBlank(amount)) {
            return new Result(true, Global.data_unright, null, null);
        }
        for (int i = 0; i < amount.length(); i++) {
            if (!Character.isDigit(amount.charAt(i))) {
                return new Result(true, Global.data_unright, null, null);
            }
        }
        Seller seller = null;
        if (!StringUtils.isNullOrBlank(token)) {
            seller = sellerService.findOne(Integer.parseInt(token));
        } else {
            seller = sellerService.findBySellerPhone(phone);
        }
        int i = Integer.parseInt(amount);
        seller.setMaxFileSize(Global.max_file_size * i);
        sellerService.update2(seller);
        return new Result(false, Global.do_success, null, null);
    }

    /**
     * 停止服务
     */
    @RequestMapping("/stop")
    public Result stop(String phone) {
        Seller seller = sellerService.findBySellerPhone(phone);
        seller.setIsActive(false);
        sellerService.update2(seller);
        return new Result(false, Global.do_success, null, null);
    }

    /**
     * 激活客户服务
     */
    @RequestMapping("/alive")
    public Result alive(String phone) {
        Seller seller = sellerService.findBySellerPhone(phone);
        seller.setIsActive(true);
        sellerService.update2(seller);
        return new Result(false, Global.do_success, null, null);
    }

    @RequestMapping("/sellers-list")
    public Result slist(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) throws IOException {

        Iterable<Seller> iterable = sellerService.list(map, pageNumber, pageSize);
        List<Seller> sellerList = new ArrayList<>();
        Iterator<Seller> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            sellerList.add(iterator.next());
        }
        return new Result(false, Global.do_success, iterable, null);
    }

    /**
     * 更新用户密码，手机号
     *
     * @param seller
     * @return
     */
    @RequestMapping("/seller_update")
    public Result seller_update(Seller seller) {
        Seller seller1 = sellerService.findBySellerPhone(seller.getSellerPhone());
        if (!StringUtils.isNullOrBlank(seller.getSellerPass())) {
            seller1.setSellerPass(seller.getSellerPass());
        }
        if (!StringUtils.isNullOrBlank(seller.getSellerPhone())) {
            seller1.setSellerPhone(seller.getSellerPhone());
        }
        sellerService.update2(seller1);
        return new Result(false, Global.do_success, null, null);
    }

    @RequestMapping("/customer-list")
    public Result clist(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return new Result(false, Global.do_success, customerService.list2(map, pageNumber, pageSize), null);
    }

    @RequestMapping("/product-list")
    public Result prolist(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        if (!StringUtils.isNullOrBlank(map.get("sellerPhone"))) {
            Seller seller = sellerService.findBySellerPhone(map.get("sellerPhone"));
            map.put("token", seller.getSellerId() + "");
        }
        return new Result(false, Global.do_success, productsService.plist(map, pageNumber, pageSize), null);
    }

    @RequestMapping("/order-list")
    public Result orderlist(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        if (!StringUtils.isNullOrBlank(map.get("sellerPhone"))) {
            Seller seller = sellerService.findBySellerPhone(map.get("sellerPhone"));
            map.put("sellerId", seller.getSellerId() + "");
        }
        return new Result(false, Global.do_success, orderService.list(map, pageNumber, pageSize), null);
    }

    @RequestMapping("/shopCar-list")
    public Object shopCarlist(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        if (!StringUtils.isNullOrBlank(map.get("show"))) {
            Iterable<ShopCar> iterable = shopCarService.list(map, pageNumber, pageSize);
            Iterator<ShopCar> iterator = iterable.iterator();
            StringBuffer stringBuffer = new StringBuffer();
            ShopCar shopCar = new ShopCar();

            while (iterator.hasNext()) {
                shopCar = iterator.next();
                stringBuffer.append("</br>");
                stringBuffer.append("时间：" + shopCar.getCreateDate() + " 商品名:" + shopCar.getProductses().get(0).getPname() + "  件数：" + shopCar.getShopCarDetails().get(0).getAmount());
                stringBuffer.append("</br>");
            }
            return stringBuffer.toString();
        } else {
            return new Result(false, Global.do_success, shopCarService.list(map, pageNumber, pageSize), null);
        }

    }

    @RequestMapping("/customer-del-all")
    public Result customerdelall() {
        customerService.deleteAll();
        return new Result(false, Global.do_success, null, null);
    }

    /**
     * 定时器，一个小时执行一次,产品部署好之后，需要手动出发该定时器
     *   http://localhost:8080/super-admin/timer?key=abcdefg&sellerId=1
     */
    @RequestMapping("/timer")
    public String timer(String key) {
        if (key.equals("abcdefg")) {
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            service.scheduleAtFixedRate(runnable_1, 60, 3600, TimeUnit.SECONDS);
            Global.timer=true;
            return "ok";
        }
        return "data_no_right";
    }

    Runnable runnable_1 = new Runnable() {
        public void run() {
            System.out.println("定时器执行了。。。。");
            //数据表 dictionarys 新增数据   访问通知
            dictionaryService.addEachDay();
            //秒杀商品更新
            productsService.autoFlush();
            Global.access_tokens.clear();
        }
    };

    //  http://www.cslapp.com/super-admin/get_timer?sellerId=1
    @RequestMapping("/get_timer")
    public Result getTimer(){
        return new Result(false,Global.do_success,Global.timer,null);
    }

}

