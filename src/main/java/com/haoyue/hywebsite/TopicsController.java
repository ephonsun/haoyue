package com.haoyue.hywebsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by LiJia on 2018/1/12.
 */
@RestController
@RequestMapping("/website/topic")
public class TopicsController {

    @Autowired
    private TopicsService topicsService;

    // 发布   /website/topic/save?username=发布者名字&userid=发布者ID&message=发布信息
    // 回复   /website/topic/save?username=发布者名字&userid=发布者ID&message=发布信息&pid=被回复信息ID
    @RequestMapping("/save")
    public Result save(Topics topics) {
        topics.setCreateDate(new Date());
        topicsService.save(topics);
        //关联父节点
        if (topics.getPid() != null && !topics.getPid().equals("")) {
            Topics parent = topicsService.findOne(Integer.parseInt(topics.getPid()));
            List<Topics> children_old = parent.getTopicsList();
            List<Topics> children_new = new ArrayList<>();
            children_new.addAll(children_old);
            children_new.add(topics);
            parent.setTopicsList(children_new);
            topicsService.save(parent);
        }
        return new Result(false, "success", null);
    }


    //  /website/topic/del?id=123
    @RequestMapping("/del")
    public Result del(int id) {
        topicsService.del(id);
        return new Result(false, "success", null);
    }

    //  /website/topic/findone?id=123
    @RequestMapping("/findone")
    public Result findone(int id) {
        Topics topics = topicsService.findOne(id);
        topicsService.addViews(id);
        return new Result(false, "success", topics);
    }

    //  https://www.cslapp.com/website/topic/list?pageNumber=0
    //  /website/topic/list?pageNumber=0&userid=当前登录用户的ID
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<Topics> iterable = topicsService.list(map, pageNumber, pageSize);
        //增加阅读数
        addViews(iterable);
        return new Result(false, "success", iterable);
    }

    public void addViews(Iterable<Topics> iterable) {
        Iterator<Topics> iterator = iterable.iterator();
        int id = 0;
        while (iterator.hasNext()) {
            id = iterator.next().getId();
            topicsService.addViews(id);
        }
    }

}
