package com.haoyue.service;

import com.haoyue.pojo.Products;
import com.haoyue.pojo.QShopCar;
import com.haoyue.pojo.ShopCar;
import com.haoyue.pojo.ShopCarDetail;
import com.haoyue.repo.ShopCarRepo;
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
        Iterable<ShopCar> iterable=findOne(shopCar.getSellerId()+"",shopCar.getCustomerId()+"",proId+"",shopcardetail.getProdutsType().getId()+"");
        Iterator<ShopCar> iterator=iterable.iterator();
        //之前有加过该商品
        if (iterator.hasNext()){
            ShopCar shopCar1=iterator.next();
            Integer amount=shopCar1.getShopCarDetails().get(0).getAmount();
            Integer addamount=shopcardetail.getAmount();
            shopCar1.getShopCarDetails().get(0).setAmount(amount+addamount);
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

    public Iterable<ShopCar> findOne(String sellerId,String customerId,String proId,String protypeId){

        QShopCar shopCar=QShopCar.shopCar;
        BooleanBuilder booleanBuilder=new BooleanBuilder();
        booleanBuilder.and(shopCar.sellerId.eq(Integer.parseInt(sellerId)));
        booleanBuilder.and(shopCar.customerId.eq(Integer.parseInt(customerId)));
        booleanBuilder.and(shopCar.productses.any().id.eq(Integer.parseInt(proId)));
        booleanBuilder.and(shopCar.shopCarDetails.any().produtsType.id.eq(Integer.parseInt(protypeId)));
        return shopCarRepo.findAll(booleanBuilder);
    }

    public Iterable<ShopCar> list(Map<String, String> map, int pageNumber, int pageSize) {
        QShopCar shopCar=QShopCar.shopCar;
        BooleanBuilder bd=new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("sellerId")){
                    bd.and(shopCar.sellerId.eq(Integer.parseInt(value)));
                }
            }
        }
        if(!StringUtils.isNullOrBlank(map.get("orderby_id"))){
            pageSize=30;
            return shopCarRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
        }
        return shopCarRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));

    }

    public List<Object> listByProducts(Map<String, String> map,String sellerId) {
        map.put("token",sellerId);
        //所有在售商品
        Iterable<Products> iterable= productsService.list(map);
        Iterator<Products> iterator=iterable.iterator();
        List<Products> products=new ArrayList<>();
        List<Object> result=new ArrayList<>();
        while (iterator.hasNext()){
            Products product=iterator.next();
            int count= shopCarRepo.findCountByPid(product.getId());
            product.setShopcar_count(count);
            products.add(product);
        }
        //排序，单个商品加入购物车人数多的在前
        products.stream()
                .sorted((p, p2) -> (p.getShopcar_count().compareTo(p2.getShopcar_count())))
                .forEach((p)->result.add(p));
        Collections.reverse(result);
        return result;
    }

    public List<String> findShopCarIdByProId(String proId) {
        List<Integer> ids= shopCarRepo.findShopCarIdByProId(proId);
        List<String> names=new ArrayList<>();
        for (Integer id:ids){
            String name=findOne(id).getWxname();
            if (names.size()!=0){
                if (names.contains(name)){
                    continue;
                }
            }
            names.add(name);
        }
        return names;
    }
}
