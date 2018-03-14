package com.haoyue.web;

import com.haoyue.pojo.MemberGroup;
import com.haoyue.service.MemberGroupService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Lijia on 2018/3/13.
 */
@RestController
@RequestMapping("/member_group")
public class MemberGroupController {

    @Autowired
    private MemberGroupService memberGroupService;


    // 保存 /member_group/save?content=内容&sellerId=3
    // /member_group/save?id=12&content=内容&sellerId=3
    @RequestMapping("/save")
    public Result save(MemberGroup memberGroup){
        memberGroupService.save(memberGroup);
        return new Result(false, Global.do_success,null,null);
    }

    //   /member_group/list?sellerId=3
    @RequestMapping("/list")
    public Result list(String sellerId){
        List<MemberGroup> list= memberGroupService.findBySellerId(sellerId);
        return new Result(false, Global.do_success,list,null);
    }

     // /member_group/del?id=12
    @RequestMapping("/del")
    public Result del(int id){
        memberGroupService.del(id);
        return new Result(false, Global.do_success,null,null);
    }


}
