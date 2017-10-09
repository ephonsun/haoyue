package com.haoyue.web;

import com.haoyue.pojo.BasicComments;
import com.haoyue.service.BasicCommentsService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by LiJia on 2017/9/25.
 */

@RestController
@RequestMapping("/basic/comments")
public class BasicCommentsController {

    @Autowired
    private BasicCommentsService basicCommentsService;

    @RequestMapping("/save")
    public Result save(BasicComments basicComments) {
        basicCommentsService.save(basicComments);
        return new Result(false, Global.do_success, basicComments, null);
    }

    @RequestMapping("/list")
    public Result shopCarlist(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return new Result(false, Global.do_success, basicCommentsService.list(map, pageNumber, pageSize), null);
    }
}
