package com.haoyue.web;

import com.haoyue.pojo.*;
import com.haoyue.service.*;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/15.
 *
 */
@RestController
@RequestMapping("/product/dailyrecord")
public class ProductDailyRecordController {

    @Autowired
    private ProductDailyRecordService productDailyRecordService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private ProductVisitorServise productVisitorServise;
    @Autowired
    private ShopCarService shopCarService;
    @Autowired
    private CollectionsService collectionsService;
    @Autowired
    private OrderService orderService;


    //   更新当日商品浏览数据  /product/dailyrecord/update?sellerId=12&pid=商品ID&openId=123
    @RequestMapping("/update")
    public Result view(String pid, String sellerId, String openId) {
        Products products = productsService.findOne(Integer.parseInt(pid));
        ProductDailyRecord productDailyRecord = productDailyRecordService.findByPidAndSellerIdAndDate(pid, sellerId);
        //如果当天该商品访问记录不存在
        if (productDailyRecord == null) {
            productDailyRecord = new ProductDailyRecord();
            productDailyRecord.setCreateDate(new Date());
            productDailyRecord.setPid(Integer.parseInt(pid));
            productDailyRecord.setPname(products.getPname());
            productDailyRecord.setViews(1);
            productDailyRecord.setVisitors(1);
            //更新被访问商品表
            updateproductvisitor(pid, sellerId, openId);
        } else {
            productDailyRecord.setViews(productDailyRecord.getViews() + 1);
            //判断被访问商品数中有没有该商品
            ProductVisitor productVisitor = productVisitorServise.findByOpenIdAndPidAndCreateDate(openId, pid);
            if (productVisitor == null) {
                productDailyRecord.setVisitors(productDailyRecord.getVisitors() + 1);
                //更新被访问商品表
                updateproductvisitor(pid, sellerId, openId);
            }
        }
        productDailyRecordService.save(productDailyRecord);
        return new Result(false, Global.do_success, null, null);
    }


    public void updateproductvisitor(String pid, String sellerId, String openId) {
        ProductVisitor productVisitor = new ProductVisitor();
        productVisitor.setCreateDate(new Date());
        productVisitor.setPid(Integer.parseInt(pid));
        productVisitor.setOpenId(openId);
        productVisitor.setSellerId(sellerId);
        productVisitorServise.save(productVisitor);
    }


    //   查看当日商品浏览数据  /product/dailyrecord/list?sellerId=12
    @RequestMapping("/list")
    public Result list(String sellerId) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        //当日商品浏览数据记录
        List<ProductDailyRecord> list1 = productDailyRecordService.findBySellerIdAndCreateDate(sellerId, new Date());
        //昨日商品浏览数据记录
        List<ProductDailyRecord> list2 = productDailyRecordService.findBySellerIdAndCreateDate(sellerId, calendar.getTime());
        //封装返回结果
        //当日所有商品浏览量，访客数
        Response response = new Response();
        if (list1 != null && list1.size() != 0) {
            for (ProductDailyRecord record : list1) {
                response.setViews(record.getViews() + response.getViews());
                response.setVisitors(record.getVisitors() + response.getVisitors());
            }
        }
        //昨日所有商品浏览量，访客数
        Response response2 = new Response();
        if (list2 != null && list2.size() != 0) {
            for (ProductDailyRecord record : list2) {
                response2.setViews(record.getViews() + response2.getViews());
                response2.setVisitors(record.getVisitors() + response2.getVisitors());
            }
        }
        //计算比率   前端将返回的比例 -1 即可 正数为上升比例 负数为下降比例
        if (response2.getViews() == 0) {
            response.setViewsRate(2);
            response.setVisitorsRate(2);
        } else {
            response.setViewsRate(response.getViews() / response2.getViews());
            response.setVisitorsRate(response.getVisitors() / response2.getVisitors());
        }
        return new Result(false, Global.do_success, response, null);
    }

    //被访问商品数
    //    /product/dailyrecord/views?sellerId=12
    @RequestMapping("/views")
    public Result views(String sellerId) {
        Date now = new Date();
        Date date = StringUtils.getYMD(now);
        //当日
        List<ProductVisitor> list1 = productVisitorServise.findBySellerIdAndCreateDate(sellerId, date);
        //前一日
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        List<ProductVisitor> list2 = productVisitorServise.findBySellerIdAndCreateDate(sellerId, calendar.getTime());
        //封装结果集
        Resonpse2 resonpse2 = new Resonpse2();
        resonpse2.setSum(list1.size());
        //计算比率  前端将返回的比例 -1 即可 正数为上升比例 负数为下降比例
        if (list1.size() == 0) {
            if (list2.size() == 0) {
                resonpse2.setRate(1);
            } else {
                resonpse2.setRate(0);
            }
        } else {
            if (list2.size() == 0) {
                resonpse2.setRate(2);
            } else {
                resonpse2.setRate(list1.size() / list2.size());
            }
        }

        return new Result(false, Global.do_success, resonpse2, null);
    }

    // 详情页跳出率
    // /product/dailyrecord/jumpout?sellerId=12
    @RequestMapping("/jumpout")
    public Result jumpout(String sellerId) {
        //获取当日商品浏览数
        Date now = new Date();
        Date date = StringUtils.getYMD(now);
        List<ProductDailyRecord> list1 = productDailyRecordService.findBySellerIdAndCreateDate(sellerId, date);
        int views = 0;
        if (list1 == null) {
            views = 0;
        } else {
            for (ProductDailyRecord record : list1) {
                views += record.getViews();
            }
        }
        //获取当日加入购物车人数
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        List<ShopCar> list2 = shopCarService.findBySellerIdAndCreateDate(sellerId, date, calendar.getTime());
        if (list2 == null) {
            list2 = new ArrayList<>();
        }
        //获取当日收藏人数
        List<Collection> list3 = collectionsService.findBySellerIdAndCreateDate(sellerId, date);
        if (list3 == null) {
            list3 = new ArrayList<>();
        }
        //获取当日订单数，不管是否付款
        List<Order> list4 = orderService.findBySellerIdAndCreateDate(sellerId, date, calendar.getTime());
        if (list4 == null) {
            list4 = new ArrayList<>();
        }

        // 当日跳出率=（商品浏览数-加入购物车人数-收藏人数-订单成交数）/商品浏览数
        double rate1 = (views - list2.size() - list3.size() - list4.size()) / views;

        //获取昨日商品浏览数
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, -1);
        views = 0;
        List<ProductDailyRecord> list11 = productDailyRecordService.findBySellerIdAndCreateDate(sellerId, calendar2.getTime());
        if (list11 == null) {
            views = 0;
        } else {
            for (ProductDailyRecord record : list11) {
                views += record.getViews();
            }
        }
        //获取昨日加入购物车人数
        List<ShopCar> list22 = shopCarService.findBySellerIdAndCreateDate(sellerId, calendar2.getTime(), date);
        if (list22 == null) {
            list22 = new ArrayList<>();
        }
        //获取昨日收藏人数
        List<Collection> list33 = collectionsService.findBySellerIdAndCreateDate(sellerId, calendar2.getTime());
        if (list33 == null) {
            list33 = new ArrayList<>();
        }
        //获取昨日订单数，不管是否付款
        List<Order> list44 = orderService.findBySellerIdAndCreateDate(sellerId, calendar2.getTime(), date);
        if (list44 == null) {
            list44 = new ArrayList<>();
        }

        // 昨日跳出率=（商品浏览数-加入购物车人数-收藏人数-订单成交数）/商品浏览数
        double rate2 = (views - list22.size() - list33.size() - list44.size()) / views;

        return new Result(false, Global.do_success, String.valueOf(rate2), String.valueOf(rate1));
    }


    class Response {
        private int views;
        private int visitors;
        private double viewsRate;
        private double visitorsRate;

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public int getVisitors() {
            return visitors;
        }

        public void setVisitors(int visitors) {
            this.visitors = visitors;
        }

        public double getViewsRate() {
            return viewsRate;
        }

        public void setViewsRate(double viewsRate) {
            this.viewsRate = viewsRate;
        }

        public double getVisitorsRate() {
            return visitorsRate;
        }

        public void setVisitorsRate(double visitorsRate) {
            this.visitorsRate = visitorsRate;
        }
    }


    class Resonpse2 {
        private int sum;
        private double rate;

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }

    // 异常商品
    // 1 获取本月之前上传的商品
    //  2 分别得到每个商品上一个月对应的总浏览量s1
    //   3  分别得到每个商品当前月对应的总浏览量s2
    //    4  对比s1和s2，如果s1小于100跳过，如果s1大于100，s2下降50%以上，该商品为异常商品
    // /product/dailyrecord/unusualProducts?sellerId=12
    @RequestMapping("/unusualproducts")
    public Result unusualProducts(String sellerId) {
        // 1 获取本月之前上传的商品
        Date date = new Date();
        date.setDate(1);
        date = StringUtils.getYMD(date);
        int num = 0;
        List<Products> list = productsService.findBySellerIdAndCreateDate(sellerId, date);
        // 如果上传的商品数量为0
        if (list == null) {
            return new Result(false, Global.do_success, num, null);
        }
        //  2 分别得到每个商品上一个月对应的总浏览量s1,小于100跳过
        int pid = 0;
        List<Products> productsList = new ArrayList<>();
        for (Products products : list) {
            num = 0;
            pid = products.getId();
            List<ProductDailyRecord> productDailyRecordList = productDailyRecordService.findByPidLastMonth(pid);
            for (ProductDailyRecord record : productDailyRecordList) {
                num += record.getViews();
            }
            if (num > 100) {
                products.setViews(num);
                productsList.add(products);
            }
        }
        //   3  分别得到每个商品当前月对应的总浏览量s2
        List<Products> productsList2 = new ArrayList<>();
        for (Products products : list) {
            num = 0;
            pid = products.getId();
            List<ProductDailyRecord> productDailyRecordList = productDailyRecordService.findByPidThisMonth(pid);
            for (ProductDailyRecord record : productDailyRecordList) {
                num += record.getViews();
            }
            products.setViews(num);
            productsList2.add(products);
        }

        //    4  对比s1和s2，s2下降50%以上，该商品为异常商品
        //如果 productsList 为空
        if (productsList.size() == 0) {
            return new Result(false, Global.do_success, 0, null);
        }
        //如果 productsList2 为空
        if (productsList2.size() == 0) {
            return new Result(false, Global.do_success, productsList, null);
        }
        //如果 productsList  productsList2 不为空
        List<Products> productsList3 = new ArrayList<>();
        for (int i = 0; i < productsList.size(); i++) {
            for (int j = 0; j < productsList2.size(); j++) {
                if (productsList.get(i).getId() == productsList2.get(j).getId()) {
                    if (productsList.get(i).getViews() / productsList2.get(j).getViews() >= 2) {
                        productsList3.add(productsList.get(i));
                    }
                }
            }
        }
        return new Result(false, Global.do_success, productsList3, null);
    }


}
