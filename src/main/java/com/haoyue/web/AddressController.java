package com.haoyue.web;

import com.haoyue.pojo.Address;
import com.haoyue.service.AddressService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiJia on 2017/9/5.
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;


    @RequestMapping("/save")
    public Result save(Address address,String openId,String sellerId){
        addressService.save(address,openId,sellerId);


        return new Result(false, Global.do_success,null,null);
    }

    @RequestMapping("/setDefult")
    public Result setDefult(int id){
        Address address=addressService.findOne(id);
        addressService.update(address);
        return new Result(false, Global.do_success,null,null);
    }

    @RequestMapping("/update")
    public Result update(Address address){
        addressService.update(address);
        return new Result(false, Global.do_success,null,null);
    }

}
