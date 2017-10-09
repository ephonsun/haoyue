package com.haoyue.web;

import com.haoyue.pojo.GroupMessage;
import com.haoyue.service.GroupMessageService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by LiJia on 2017/9/7.
 */
@RestController
@RequestMapping("/groupMessage")
public class GroupMessageController {

    @Autowired
    private GroupMessageService groupMessageService;

    @RequestMapping("/save")
    public Result save(GroupMessage groupMessage){
        groupMessage.setCreateDate(new Date());
        groupMessage=groupMessageService.save(groupMessage);
        return new Result(false, Global.do_success,groupMessage,null);
    }

    @RequestMapping("findOne")
    public Result findOne(Integer id,String wxName ,String pics){
        GroupMessage groupMessage=groupMessageService.findOne(id);
        groupMessage.setWxName(groupMessage.getWxName()+"/../"+wxName);
        groupMessage.setPics(groupMessage.getPics()+"/../"+pics);
        groupMessageService.save(groupMessage);
        return new Result(false,Global.do_success,groupMessageService.findOne(id),null);
    }


}
