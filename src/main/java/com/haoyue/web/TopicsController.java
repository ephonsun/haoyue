package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.Order;
import com.haoyue.pojo.Topics;
import com.haoyue.service.TopicsService;
import com.haoyue.untils.Global;
import com.haoyue.untils.OSSClientUtil;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

/**
 * Created by Lijia on 2018/5/4.
 */

@RestController
@RequestMapping("/topic")
public class TopicsController {

    @Autowired
    private TopicsService topicsService;


    //  /topic/save?sellerId=3&openId=123&wxname=微信名称&pics=图片外链&message=文字内容
    @RequestMapping("/save")
    public Result save(Topics topics) {
        topics.setCreateDate(new Date());
        topicsService.save(topics);
        return new Result(false, Global.do_success, topics, null);
    }


    //  /topic/uploadPics?multipartFiles=图片&sellerId=卖家ID
    @RequestMapping("/uploadPics")
    public Result uploadPics(MultipartFile[] multipartFiles, Integer sellerId) throws MyException {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (Global.object4) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (MultipartFile multipartFile : multipartFiles) {
                OSSClientUtil ossClientUtil = new OSSClientUtil();
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                stringBuffer.append(Global.aliyun_href + uploadUrl);
                stringBuffer.append(",");
            }
        }
        return new Result(false, Global.do_success, stringBuffer.toString(), null);
    }

    //  /topic/list?sellerId=3&pageNumber=页数(从0开始)
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<Topics> iterable = topicsService.list(map, pageNumber, pageSize);
        return new Result(false, Global.do_success, iterable, null);
    }


    //  /topic/del?id=记录ID&sellerId=3
    @RequestMapping("/del")
    public Result del(Integer id,String sellerId){
        Topics topics=topicsService.findOne(id);
        topics.setActive(false);
        topicsService.update(topics);
        return new Result(false, Global.do_success, null, null);
    }

}
