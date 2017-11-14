package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TAddress;
import com.haoyue.tuangou.service.TAddressService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/11/13.
 */
@RestController
@RequestMapping("/tuan/taddress")
public class TAddressController {

    @Autowired
    private TAddressService addressService;


    // http://localhost:8080/tuan/taddress/save?openId=12&saleId=21&receiver=收件人&phone=电话&address=地址
    @RequestMapping("/save")
    public TResult save(TAddress address){
        TAddress tAddress=addressService.findByOpenIdAndSaleId(address.getOpenId(),address.getSaleId());
        if (tAddress==null){
            tAddress=new TAddress();
        }
        tAddress.setAddress(address.getAddress());
        tAddress.setReceiver(address.getReceiver());
        tAddress.setPhone(address.getPhone());
        tAddress.setOpenId(address.getOpenId());
        tAddress.setSaleId(address.getSaleId());
        addressService.save(tAddress);
        return new TResult(false, TGlobal.do_success,null);
    }


    // http://localhost:8080/tuan/taddress/findone?openId=12&saleId=21
    @RequestMapping("/findone")
    public TResult findOne(String openId,String saleId){
        TAddress tAddress=addressService.findByOpenIdAndSaleId(openId,saleId);
        return new TResult(false, TGlobal.do_success,tAddress);
    }

}
