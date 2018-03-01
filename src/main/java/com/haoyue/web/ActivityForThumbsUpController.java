package com.haoyue.web;

import com.haoyue.pojo.ActivityForThumbsUp;
import com.haoyue.service.ActivityForThumbsUpService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/activity/thumbs_up")
public class ActivityForThumbsUpController {

    @Autowired
    private ActivityForThumbsUpService activityForThumbsUpService;


    // /activity/thumbs_up/save?openId=123&wxname=微信名称&wxpic=微信头像&isowner=【自己申请 true/ 帮好友点赞 false】
    @RequestMapping("/save")
    public Result save(ActivityForThumbsUp activityForThumbsUp, int pid) {
        Date date = new Date();
        activityForThumbsUp.setCreateDate(date);
        //申请之前判断是否之前已经申请过
        if (activityForThumbsUp.getIsIsowner()) {
            ActivityForThumbsUp old = activityForThumbsUpService.findOpenIdAndIsowner(activityForThumbsUp.getOpenId(), true);
            //存在且未过期
            if (old != null) {
                return new Result(true, Global.already_apply_thumb_up, null, null);
            }
            //存在且已经过期(删除旧的数据)
//           if (old!=null&&old.getEndDate().before(date)){
//               activityForThumbsUpService.delete(old);
//           }
            //结束时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 132);//5天12小时
            activityForThumbsUp.setEndDate(calendar.getTime());
            activityForThumbsUpService.save(activityForThumbsUp);
        }
        //是否帮别人点赞
        if (!activityForThumbsUp.getIsIsowner()) {
            ActivityForThumbsUp parent = activityForThumbsUpService.findOne(pid);
            List<ActivityForThumbsUp> childs = parent.getList();
            //判断是否已经过期
            if (parent.getEndDate().before(date)) {
                return new Result(true, Global.already_thumb_up_expire, null, null);
            }
            //判断是否是自己为自己点赞
            if (parent.getOpenId().equals(activityForThumbsUp.getOpenId())) {
                return new Result(true, Global.can_not_thumb_up_self, null, null);
            }
            //判断积攒数量是否已经满了
            if (childs.size() == Global.thumbs_up_num) {
                return new Result(true, Global.thumbs_up_access, null, null);
            }
            //判断是否之前已经帮忙点赞
            for (ActivityForThumbsUp activity : childs) {
                if (activity.getOpenId().equals(activityForThumbsUp.getOpenId())) {
                    return new Result(true, Global.already_help_thumb_up, null, null);
                }
            }
            activityForThumbsUpService.save(activityForThumbsUp);
            List<ActivityForThumbsUp> new_childs = new ArrayList<>();
            new_childs.addAll(childs);
            new_childs.add(activityForThumbsUp);
            parent.setList(new_childs);
            activityForThumbsUpService.save(parent);
        }
        return new Result(false, Global.do_success, activityForThumbsUp, null);
    }


    //  https://www.cslapp.com/activity/thumbs_up/findone?openId=oARLy0NxkpShwBMwIpS6JJ-dXnLE
    @RequestMapping("/findone")
    public Result findOne(String openId) {
        ActivityForThumbsUp activityForThumbsUp = activityForThumbsUpService.findOpenIdAndIsowner(openId, true);
        return new Result(false, Global.do_success, activityForThumbsUp, null);
    }

    // /activity/thumbs_up/list?isowner=true
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<ActivityForThumbsUp> iterable = activityForThumbsUpService.list(map, pageNumber, pageSize);
        return new Result(false, Global.do_success, iterable, null);
    }


}
