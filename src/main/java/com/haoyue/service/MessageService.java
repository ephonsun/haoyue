package com.haoyue.service;

import com.haoyue.pojo.AfterSale;
import com.haoyue.pojo.Customer;
import com.haoyue.pojo.Message;
import com.haoyue.pojo.Seller;
import com.haoyue.repo.AfterSaleRepo;
import com.haoyue.repo.CustomerRepo;
import com.haoyue.repo.MessageRepo;
import com.haoyue.repo.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private AfterSaleRepo afterSaleRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private SellerRepo sellerRepo;

    public void save(String string,int afterSaleId,boolean isCustomer){
        Message message=new Message();
        message.setAfter_sale_id(afterSaleId);
        message.setContent(string);
        message.setCreateDate(new Date());
        AfterSale afterSale=afterSaleRepo.findOne(afterSaleId);
        //买家协商
        if(isCustomer){
            Customer customer=customerRepo.findOne(afterSale.getOrder().getCustomerId());
            message.setWxname(customer.getWxname());
            message.setWxpic(customer.getWxpic());
        }
        //卖家协商
        else {
            Seller seller=sellerRepo.findOne(afterSale.getOrder().getSellerId());
            message.setSellerName(seller.getSellerName());
        }
        messageRepo.save(message);
    }

    public List<Message> findByAfterSaleId(int afterSaleId){
       return messageRepo.findByAfter_sale_id(afterSaleId);
    }

}
