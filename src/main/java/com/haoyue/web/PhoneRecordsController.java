package com.haoyue.web;

import com.haoyue.pojo.Member;
import com.haoyue.pojo.PhoneRecords;
import com.haoyue.service.PhoneRecordsService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * Created by Lijia on 2018/7/3.
 */

@RestController
@RequestMapping("/phoneRecords")
public class PhoneRecordsController {

    @Autowired
    private PhoneRecordsService phoneRecordsService;


    //  /phoneRecords/save?phone=电话号码&sellerId=3
    @RequestMapping("/save")
    public Result save(String phones, String sellerId) {

        if (!StringUtils.isNullOrBlank(phones)) {
            String[] phone = phones.split("-");
            if (phone != null && phone.length != 0) {
                for (int i = 0; i < phone.length; i++) {
                    String each = phone[i];
                    PhoneRecords phoneRecords = new PhoneRecords();
                    phoneRecords.setCreateDate(new Date());
                    phoneRecords.setPhone(each);
                    phoneRecords.setSellerId(sellerId);
                    phoneRecordsService.save(phoneRecords);
                }
            }
        }

        return new Result(false, Global.do_success, null, null);
    }

    @RequestMapping("/list")
    public Result list_new(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<PhoneRecords> iterable = phoneRecordsService.list(map, pageNumber, pageSize);
        return new Result(false, Global.do_success, iterable, null);
    }

    @RequestMapping("/delete")
    public Result del(String id,String sellerId){
        PhoneRecords phoneRecords=phoneRecordsService.findOne(id);
        if (phoneRecords.getSellerId().equals(sellerId)){
            phoneRecordsService.delete(id);
            return new Result(false, Global.do_success, null, null);
        }else {
            return new Result(true, Global.have_no_right, null, null);
        }
    }

    

}
