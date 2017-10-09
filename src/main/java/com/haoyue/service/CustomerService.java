package com.haoyue.service;

import com.haoyue.pojo.Customer;
import com.haoyue.pojo.QCustomer;
import com.haoyue.pojo.QShopCar;
import com.haoyue.pojo.ShopCar;
import com.haoyue.repo.CustomerRepo;
import com.haoyue.repo.ShopCarRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LiJia on 2017/9/4.
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private ShopCarRepo shopCarRepo;
    @Autowired
    private DictionaryService dictionaryService;

    public Customer findByPhone(String phone) {
        return customerRepo.findByPhone(phone);
    }


    public Iterable<ShopCar> list(Map<String, String> map ) {

        QShopCar shopCar =QShopCar.shopCar;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("cid")){
                    bd.and(shopCar.customerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("sellerId")){
                   bd.and(shopCar.sellerId.eq(Integer.parseInt(value)));
                }
            }
        }
        return shopCarRepo.findAll(bd.getValue());
    }

    public void update(Customer customer) {
        customerRepo.save(customer);
    }

    public Customer save(Customer customer) {
        return  customerRepo.save(customer);
    }

    public Customer findByOpenId(String openId,String sellerId) {
        return customerRepo.findByOpenIdAndSellerId(openId,sellerId);
    }

    public Iterable<Customer> list2(Map<String, String> map,int pageNumber,int pageSzie ) {

        QCustomer customer = QCustomer.customer;
        BooleanBuilder bd = new BooleanBuilder();

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {


            }
        }

        return customerRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSzie));
    }

    public void deleteAll() {
        customerRepo.deleteAll();
    }
}
