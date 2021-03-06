package com.haoyue.web;

import com.aliyuncs.exceptions.ClientException;
import com.google.gson.JsonObject;
import com.haoyue.Exception.MyException;
import com.haoyue.pojo.*;
import com.haoyue.pojo.Dictionary;
import com.haoyue.service.*;
import com.haoyue.untils.*;
import net.sf.json.util.JSONUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by LiJia on 2017/8/21.
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private CouponService couponService;

    @RequestMapping("/reg")
    @Transactional
    public Result regist(Seller seller) throws MyException {
        return sellerService.save(seller);
    }

    @RequestMapping("/login")
    public Result login(Seller seller) {

        try {
            Seller seller1 = sellerService.login(seller);
            if (seller1 == null) {
                return new Result(Global.user_isnull, true);
            }
            //判断店铺是否过期
            boolean flag = sellerService.isStop(seller1.getSellerId() + "");
            if (flag) {
                return new Result(false, Global.service_stop, null, null);
            }
            //刷新 online_code
            seller1.setOnlineCode(new Date().getTime() + "");
            sellerService.update(seller1);

            SellerUtils.hidePass(seller1);
            return new Result(false, Global.do_success, seller1, seller1.getSellerId() + "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, Global.do_fail + e);
        }
    }

    // /seller/findOne?token=3
    @RequestMapping("/findOne")
    public Result findOne(@RequestParam Map<String, String> map) throws IOException {

        String token = "";
        if (!StringUtils.isNullOrBlank(map.get("token"))) {
            token = map.get("token");
        }
        Iterable all = sellerService.filter(map);
        Iterator<Seller> iterator = all.iterator();
        if (iterator.hasNext()) {
            Seller seller = iterator.next();
            SellerUtils.hidePass(seller);
            return new Result(true, Global.record_exist, seller, token);
        }
        return new Result(false, Global.record_unexist, null, token);
    }

    @RequestMapping("/quit")
    public Result quit(String token) {
        System.out.println(token);
        return new Result(false, Global.do_success, StringUtils.getYMD(new Date()), null);
    }

    @RequestMapping("/checkCode")
    public Result checkCode() {
        return new Result(StringUtils.checkCode(4));
    }

    @RequestMapping("/sort")
    public Result sortByturnover(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {

        //全部 一个月
        if (!StringUtils.isNullOrBlank(map.get("all"))) {
            Iterable<Dictionary> list = dictionaryService.findBySellerId2(Integer.parseInt(map.get("token")), pageNumber, pageSize);
            return new Result(false, Global.do_success, list, map.get("token"));
        }
        Iterable<Order> iterable = orderService.list(map, pageNumber, pageSize);
        //当月所有订单
        Iterator<Order> iterator = iterable.iterator();
        Set set = new HashSet();
        List<IndexResult> indexResults = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        //得到当月订单中所有不同商品的ID
        while (iterator.hasNext()) {
            Order order = iterator.next();
            orders.add(order);
            if (set.contains(order.getProducts().get(0).getId())) {
                continue;
            }
            set.add(order.getProducts().get(0).getId());
        }
        if (set.isEmpty()) {
            return null;
        }
        //遍历set，得到指定ID的商品
        Iterator<Integer> iterator1 = set.iterator();
        Integer pid = 0;
        Integer i = 1;
        while (iterator1.hasNext()) {
            pid = iterator1.next();
            //把不同Id商品添加到 indexResult
            IndexResult indexResult = new IndexResult();
            for (Order order : orders) {
                //封装返回数据
                if (order.getProducts().get(0).getId() == pid) {
                    //交易额
                    indexResult.setTurnover(order.getPrice() + indexResult.getTurnover());
                    //买家数
                    indexResult.setBuyers(i++);
                    if (i == 2) {
                        //浏览量 访客数
                        com.haoyue.pojo.Dictionary dictionary = dictionaryService.findByProductId(pid);
                        indexResult.setProductses(order.getProducts().get(0));
                        indexResult.setVisitors(dictionary.getVisitors());
                        indexResult.setViews(dictionary.getViews());
                    }
                    indexResults.add(indexResult);
                }
            }
        }
        if (!StringUtils.isNullOrBlank(map.get("turnover"))) {
            //交易额排序
            Collections.sort(indexResults, new Comparator<IndexResult>() {
                @Override
                public int compare(IndexResult o1, IndexResult o2) {
                    return o1.getTurnover().compareTo(o2.getTurnover());
                }
            });
        } else if (!StringUtils.isNullOrBlank(map.get("buyers"))) {
            //买家数排序
            Collections.sort(indexResults, new Comparator<IndexResult>() {
                @Override
                public int compare(IndexResult o1, IndexResult o2) {
                    return o1.getBuyers().compareTo(o2.getBuyers());
                }
            });
        } else if (!StringUtils.isNullOrBlank(map.get("visitors"))) {
            //访客数排序
            Collections.sort(indexResults, new Comparator<IndexResult>() {
                @Override
                public int compare(IndexResult o1, IndexResult o2) {
                    return o1.getViews().compareTo(o2.getViews());
                }
            });
        } else if (!StringUtils.isNullOrBlank(map.get("views"))) {
            //浏览数排序
            Collections.sort(indexResults, new Comparator<IndexResult>() {
                @Override
                public int compare(IndexResult o1, IndexResult o2) {
                    return o1.getVisitors().compareTo(o2.getVisitors());
                }
            });
        }
        return new Result(indexResults, map.get("token"));
    }

    // /seller/update?token=3&service_template_msg=访问通知信息内容
    @RequestMapping("/update")
    public Object update(Seller seller, MultipartFile[] files, String token) throws IOException, MyException {
        seller.setSellerId(Integer.parseInt(token));
        Seller seller1 = sellerService.findOne(Integer.parseInt(token));
        String oldvideo = seller1.getVideos();
        String oldpics = seller1.getBanners();
        OSSClientUtil ossClientUtil = new OSSClientUtil();
        if (files != null && files.length != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            seller.setUploadFileSize(seller1.getUploadFileSize());
            for (int i = 0; i < files.length; i++) {
                MultipartFile multipartFile = files[i];
                long size = multipartFile.getSize();
                int kb = (int) size / 1024;
                if (kb > 102400) {
                    return new UploadFailResult(Global.file_toolarge);
                }
                if ((seller1.getUploadFileSize() + kb) > seller1.getMaxFileSize()) {
                    return new UploadFailResult(Global.memoryspace_lack);
                }
                //返回上传成功的文件名
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                seller.setUploadFileSize(seller.getUploadFileSize() + kb);
                //判断是不是视频
                String end = uploadUrl.substring(uploadUrl.indexOf(".") + 1);
                if (end.equalsIgnoreCase("mp4") || end.equalsIgnoreCase("avi") || end.equalsIgnoreCase("rmvb")) {
                    seller.setVideos(Global.aliyun_href + uploadUrl);
                    //删除原来的视频
                    if (!StringUtils.isNullOrBlank(oldvideo)) {
                        ossClientUtil.delete(oldvideo.substring(oldvideo.indexOf("hymarket")));
                    }
                    continue;
                }
                stringBuffer.append(Global.aliyun_href + uploadUrl);
                //去除最后一个多余的逗号
                if (i != files.length - 1) {
                    stringBuffer.append(",");
                }
            }

            seller.setBanners(stringBuffer.toString());
        }
        sellerService.update(seller);
        return new UploadSuccessResult(Global.do_success);
    }


    //  /seller/uploadPics?sellerId=12&multipartFiles=上传的文件
    @RequestMapping("/uploadPics")
    public Object uploadPics(MultipartFile[] multipartFiles, Integer sellerId) throws MyException {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (Global.object4) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (MultipartFile multipartFile : multipartFiles) {
                OSSClientUtil ossClientUtil = new OSSClientUtil();
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                stringBuffer.append(Global.aliyun_href+uploadUrl);
                stringBuffer.append(",");
            }
        }
        return new UploadSuccessResult(stringBuffer.toString()) ;
    }

    @RequestMapping("/checkPass")
    public Result checkPass(String oldPass, String token) {
        Seller seller = sellerService.findOne(Integer.parseInt(token));
        if (seller.getSellerPass().equals(oldPass)) {
            return new Result(false, Global.oldPass_Rigt, token);
        }
        return new Result(false, Global.oldPass_unRigt, token);
    }

    // /seller/getPhoneCode?token=卖家Id  13732612020
    @RequestMapping("/getPhoneCode")
    public Result forgetPass(String phone, String token) {
        if (StringUtils.isNullOrBlank(phone)) {
            Seller seller = sellerService.findOne(Integer.parseInt(token));
            String code = StringUtils.getPhoneCode();
            try {
                SendCode.sendSms(seller.getSellerPhone(), code);
            } catch (ClientException e) {
                e.printStackTrace();
                return new Result(false, Global.do_fail);
            }
            return new Result(false, Global.do_success, code, token);
        } else {
            String code = StringUtils.getPhoneCode();
            if(StringUtils.isNullOrBlank(token)){
                try {
                    SendCode.sendSms(phone, code);
                } catch (ClientException e) {
                    e.printStackTrace();
                    return new Result(false, Global.do_fail);
                }
            }else {
                Seller seller = sellerService.findBySellerPhone(phone);
                if (seller == null) {
                    return new Result(true, Global.phone_isnull, null, null);
                }
                try {
                    SendCode.sendSms(phone, code);
                } catch (ClientException e) {
                    e.printStackTrace();
                    return new Result(false, Global.do_fail);
                }
            }
            return new Result(false, Global.do_success, code, token);
        }
    }

    @RequestMapping("/changePass")
    public Result changePassByPhoneCode(String phone, String newPass, String token) {
        Seller seller = sellerService.findBySellerPhone(phone);
        seller.setSellerPass(newPass);
        sellerService.update(seller);
        return new Result(false, Global.do_success);
    }

    /**
     * 小程序端店铺首页
     *
     * @param token
     * @param pageSize
     * @return
     * https://www.cslapp.com/seller/index?token=3
     */
    @RequestMapping("/index")
    public Result index(Integer token, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        Integer sellerId = token;
        Seller seller = sellerService.findOne(sellerId);
        List<Object> objects = new ArrayList<>();
        objects.add(seller.getBanners());
        objects.add(seller.getVideos());
        Map<String, String> map = new HashMap<>();
        map.put("token", sellerId + "");
        map.put("active", "true");
        map.put("showdate","yes");
        map.put("orderById","yes");
        Iterable<Products> iterable = productsService.plist(map, Global.pageNumber, pageSize);
        objects.add(iterable);
        objects.add(seller.getLunbo());
        return new Result(objects);
    }

    /**
     * 转发
     *
     * @param indexTitle
     * @param pallTitle
     * @param pdescTitle
     * @param token
     * @return
     */
    @RequestMapping("/forward")
    public Result forward(String indexTitle, String pallTitle, String pdescTitle, String token) {
        Integer sellerId = Integer.parseInt(token);
        Seller seller = sellerService.findOne(sellerId);
        if (!StringUtils.isNullOrBlank(indexTitle)) {
            seller.setIndexTitle(indexTitle);
        }
        if (!StringUtils.isNullOrBlank(pallTitle)) {
            seller.setPallTitle(pallTitle);
        }
        if (!StringUtils.isNullOrBlank(pdescTitle)) {
            seller.setPallTitle(pdescTitle);
        }
        sellerService.update2(seller);
        return new Result(false, Global.do_success, seller, null);
    }

    /**
     * 开启/关闭 优惠券功能
     *
     * @param sellerId
     * @param flag
     * @return
     */
    @RequestMapping("/deal-coupon")
    public Result openCoupon(String sellerId, Boolean flag) {
        Seller seller = sellerService.findOne(Integer.parseInt(sellerId));
        //如果是关闭优惠券活动，则清空本次的数据
        if (flag == false) {
            couponService.deleteBySellerId(sellerId);
        }
        seller.setIscoupon(flag);
        sellerService.update2(seller);
        return new Result(false, Global.do_success, null, null);
    }

    // 获取退货时的收货地址  /seller/get_receive_address?sellerId=卖家ID
    @RequestMapping("/get_receive_address")
    public Result  getReceiveAddress(String sellerId){
        Seller seller=sellerService.findOne(Integer.parseInt(sellerId));
        return new Result(false, Global.do_success, seller.getReceiveAddress(), null);
    }


    /**
     * 离线操作
     *
     * @param token
     * @return
     */
    @RequestMapping("/out_line")
    public Result out_line(String token) {
        Seller seller = sellerService.findOne(Integer.parseInt(token));
        //seller.setIsout(false);
        sellerService.update2(seller);
        return new Result(false, Global.do_success, null, null);
    }

    // http://localhost:8080/seller/out_line?sellerId=3
    @RequestMapping("/test")
    public Result test(){
        System.out.println("123");
        System.out.println("456");
        return new Result(false, Global.do_success, null, null);
    }


//# 小程序个人中心设置充值中心模块，可以进行选择充值卡充值，查询自己历史充值，查询余额
//# 卖家后台设置充值管理，充值卡管理，用户充值列表
//#  需要新建充值付款接口  使用余额付款需要额外支付至少一分钱的现金

}
