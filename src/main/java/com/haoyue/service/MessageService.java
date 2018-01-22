package com.haoyue.service;

import com.haoyue.pojo.Message;
import com.haoyue.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void save(String string,int afterSaleId){
        Message message=new Message();
        message.setAfter_sale_id(afterSaleId);
        message.setContent(string);
        message.setCreateDate(new Date());
        messageRepo.save(message);
    }

    public List<Message> findByAfterSaleId(int afterSaleId){
       return messageRepo.findByAfter_sale_id(afterSaleId);
    }

}
