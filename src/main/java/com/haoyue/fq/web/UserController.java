package com.haoyue.fq.web;

import com.haoyue.fq.pojo.User;
import com.haoyue.fq.service.UserService;
import com.haoyue.fq.utils.Global;
import com.haoyue.fq.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by LiJia on 2017/12/25.
 */
@RestController
@RequestMapping("/fq/user")
public class UserController {

    @Autowired
    private UserService userService;

    //  /fq/user/login?wxname=微信昵称&wxpic=微信头像&openId=12133
    @RequestMapping("/login")
    public Result login(User user) {
        User user1 = userService.findByOpenId(user.getOpenId());
        if (user1 == null) {
            user1 = new User();
            user1.setCreateDate(new Date());
            user1.setOpenId(user.getOpenId());
        }
        user1.setWxname(user.getWxname());
        user1.setWxpic(user.getWxpic());
        user1.setRate(user1.getExpenses() - user1.getReal_expenses());
        userService.save(user1);
        return new Result(false, Global.do_success, user1);
    }

    //  /fq/user/update?openId=12133&expenses=预算支出
    @RequestMapping("/update")
    public Result update(String openId, Double expenses) {
        User user = userService.findByOpenId(openId);
        user.setExpenses(expenses);
        userService.save(user);
        return new Result(false, Global.do_success, null);
    }

    //  /fq/user/findOne?openId=12133
    @RequestMapping("/findone")
    public Result findOne(String openId) {
        User user = userService.findByOpenId(openId);
        user.setRate(user.getExpenses() - user.getReal_expenses());
        return new Result(false, Global.do_success, user);
    }

}
