package com.haoyue.service;

import com.haoyue.pojo.*;
import com.haoyue.repo.ShopCarRepo;
import com.haoyue.untils.CommonUtil;
import com.haoyue.untils.Global;
import com.haoyue.untils.HttpRequest;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by LiJia on 2017/9/4.
 */
@Service
public class ShopCarService {

    @Autowired
    private ShopCarRepo shopCarRepo;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private ShopCarDetailService shopCarDetailService;

    public ShopCar findOne(Integer id) {
        return shopCarRepo.findOne(id);
    }

    public void del(Integer id) {
        shopCarRepo.delete(id);
    }

    public ShopCar save(ShopCar shopCar, Integer proId, ShopCarDetail shopcardetail) {

        //首先判断之前该商品有没有加入过购物车
        Iterable<ShopCar> iterable = findOne(shopCar.getSellerId() + "", shopCar.getCustomerId() + "", proId + "", shopcardetail.getProdutsType().getId() + "");
        Iterator<ShopCar> iterator = iterable.iterator();
        //之前有加过该商品
        if (iterator.hasNext()) {
            ShopCar shopCar1 = iterator.next();
            Integer amount = shopCar1.getShopCarDetails().get(0).getAmount();
            Integer addamount = shopcardetail.getAmount();
            shopCar1.getShopCarDetails().get(0).setAmount(amount + addamount);
            return shopCarRepo.save(shopCar1);
        }
        //第一次加入该商品到购物车
        else {
            Products products = productsService.findOne(proId);
            List<Products> lists = new ArrayList<>();
            lists.add(products);
            shopCar.setProductses(lists);
            shopCarDetailService.save(shopcardetail);
            List<ShopCarDetail> shopCarDetails = new ArrayList<>();
            shopCarDetails.add(shopcardetail);
            shopCar.setShopCarDetails(shopCarDetails);
            return shopCarRepo.save(shopCar);
        }
    }

    public Iterable<ShopCar> findOne(String sellerId, String customerId, String proId, String protypeId) {

        QShopCar shopCar = QShopCar.shopCar;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(shopCar.sellerId.eq(Integer.parseInt(sellerId)));
        booleanBuilder.and(shopCar.customerId.eq(Integer.parseInt(customerId)));
        booleanBuilder.and(shopCar.productses.any().id.eq(Integer.parseInt(proId)));
        booleanBuilder.and(shopCar.shopCarDetails.any().produtsType.id.eq(Integer.parseInt(protypeId)));
        return shopCarRepo.findAll(booleanBuilder);
    }

    public Iterable<ShopCar> list(Map<String, String> map, int pageNumber, int pageSize) {
        QShopCar shopCar = QShopCar.shopCar;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("sellerId")) {
                    bd.and(shopCar.sellerId.eq(Integer.parseInt(value)));
                }
            }
        }
        if (!StringUtils.isNullOrBlank(map.get("orderby_id"))) {
            pageSize = 30;
            return shopCarRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));
        }
        return shopCarRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));

    }

    public List<Object> listByProducts(Map<String, String> map, String sellerId) {
        map.put("token", sellerId);
        //所有在售商品
        Iterable<Products> iterable = productsService.list(map);
        Iterator<Products> iterator = iterable.iterator();
        List<Products> products = new ArrayList<>();
        List<Object> result = new ArrayList<>();
        while (iterator.hasNext()) {
            Products product = iterator.next();
            int count = shopCarRepo.findCountByPid(product.getId());
            product.setShopcar_count(count);
            products.add(product);
        }
        //排序，单个商品加入购物车人数多的在前
        products.stream()
                .sorted((p, p2) -> (p.getShopcar_count().compareTo(p2.getShopcar_count())))
                .forEach((p) -> result.add(p));
        Collections.reverse(result);
        return result;
    }

    public List<String> findShopCarIdByProId(String proId) {
        List<Integer> ids = shopCarRepo.findShopCarIdByProId(proId);
        List<String> names = new ArrayList<>();
        for (Integer id : ids) {
            String name = findOne(id).getWxname();
            if (names.size() != 0) {
                if (names.contains(name)) {
                    continue;
                }
            }
            names.add(name);
        }
        return names;
    }

    public List<ShopCar> findBySellerIdAndCreateDate(String sellerId, Date from, Date end) {
        return shopCarRepo.findBySellerIdAndCreateDate(sellerId, from, end);
    }


    public void sendCustomerWxTemplate(Integer protypeId, Integer sellerId) {
        //刷新shopcar里的所有formId状态
        shopCarRepo.updateActiveFalseByDate(new Date());
        //取出所有没有过期的formId关联的订单
        List<ShopCar> shopcars = shopCarRepo.findActiveIsTrue(sellerId);
        for (ShopCar shopcar : shopcars) {
            //判断protypeId是否一样
            if (shopcar.getShopCarDetails() != null) {
                if (shopcar.getShopCarDetails().get(0).getProdutsType().getId() == protypeId) {
                    String pname = shopcar.getProductses().get(0).getPname();
                    int pid = shopcar.getProductses().get(0).getId();
                    //发送模板信息 过滤掉 the formId is a mock one 和 undefined
                    // ||shopcar.getFormId().equals("undefined")||shopcar.getFormId2().equals("undefined")
                    if (shopcar.getFormId().equals("the formId is a mock one") || shopcar.getFormId2().equals("the formId is a mock one")) {
                        continue;
                    }
                    if (StringUtils.isNullOrBlank(shopcar.getFormId())) {
                        //formId2
                        if (!shopcar.getFormId2().equals("undefined")) {
                            addTemplate(pid, pname, shopcar.getOpenId(), shopcar.getShopCarDetails().get(0).getProdutsType(), shopcar.getFormId2());
                        }
                        shopcar.setFormId2(null);
                        shopCarRepo.updateFormId2(shopcar.getId());
                    } else {
                        //formId
                        if (!shopcar.getFormId().equals("undefined")) {
                            addTemplate(pid, pname, shopcar.getOpenId(), shopcar.getShopCarDetails().get(0).getProdutsType(), shopcar.getFormId());
                        }
                        shopcar.setFormId(null);
                        shopCarRepo.updateFormId(shopcar.getId());
                    }
                    if (StringUtils.isNullOrBlank(shopcar.getFormId()) && StringUtils.isNullOrBlank(shopcar.getFormId2())) {
                        //更新shop.active=false formId formId2 已过期或已被使用
                        shopCarRepo.updateActiveFalse(shopcar.getId());
                    }
                }
            }
        }
    }


    public void getTemplate(Template template, String formId) {
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=wxe46b9aa1b768e5fe&secret=8bcdb74a9915b5685fa0ec37f6f25b24";
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        //发送模板信息
        template.setForm_id(formId);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + formId;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
    }

    public void addTemplate(int pid, String pname, String openId, ProdutsType ptype, String formId) {

        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(String.valueOf(ptype.getDiscountPrice()));
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(pname);
        list.add(templateResponse2);

        TemplateResponse templateResponse3 = new TemplateResponse();
        templateResponse3.setColor("#0000ff");
        templateResponse3.setName("keyword3");
        templateResponse3.setValue("点击进入");
        list.add(templateResponse3);

        TemplateResponse templateResponse4 = new TemplateResponse();
        templateResponse4.setColor("#000000");
        templateResponse4.setName("keyword4");
        templateResponse4.setValue("近期好价");
        list.add(templateResponse4);

        Template template = new Template();
        template.setTemplateId("HsbxE0x_CqdmCu6u0hhYtGB4Ry2f_R9M96KBLLxWbUM");
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage("pages/details/details?id=" + pid + "&ptypeId=" + ptype.getId());
        template.setToUser(openId);
        getTemplate(template, formId);

    }
}
