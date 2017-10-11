package com.haoyue.web;

import com.haoyue.pojo.LeaveMessage;
import com.haoyue.service.LeaveMessageService;
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
 * Created by LiJia on 2017/9/30.
 * 官网网站留言
 */
@RestController
@RequestMapping("/leave-message")
public class LeaveMessageController {

    @Autowired
    private LeaveMessageService leaveMessageService;

    @RequestMapping("/save")
    public Result save(LeaveMessage leaveMessage) {
        leaveMessage.setCreateDate(new Date());
        leaveMessageService.save(leaveMessage);
        return new Result(false, Global.do_success, leaveMessage, null);
    }

    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return new Result(false, Global.do_success, leaveMessageService.list(map, pageNumber, pageSize), null);
    }

    @RequestMapping("/del")
    public Result del(String id, String admin_id) {
        if (StringUtils.isNullOrBlank(admin_id)) {
            return new Result(false, Global.data_unright, null, null);
        }
        leaveMessageService.del(id);
        return new Result(false, Global.do_success, null, null);
    }

}
