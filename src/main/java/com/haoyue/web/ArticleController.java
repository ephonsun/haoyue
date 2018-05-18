package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.Article;
import com.haoyue.pojo.Member;
import com.haoyue.service.ArticleService;
import com.haoyue.untils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

/**
 * Created by Lijia on 2018/5/17.
 */

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    // /article/save?sellerId=1&author=作者&titile=标题&remark=摘要&indexpic=首页图片地址&pics=内容图片地址&content=内容（<br> 分割多个文本框）
    @RequestMapping("/save")
    public Result save(Article article){
        if(article.getId()==null){
            article.setCreateDate(new Date());
        }
        articleService.save(article);
        return new Result(false, Global.do_success,article,null);
    }

    //上传图片  /article/uploadPics
    @RequestMapping("/uploadPics")
    public UploadSuccessResult uploadPics(MultipartFile[] multipartFiles, Integer sellerId) throws MyException {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (Global.object4) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i=0;i<multipartFiles.length;i++) {
                MultipartFile multipartFile=multipartFiles[i];
                OSSClientUtil ossClientUtil = new OSSClientUtil();
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                stringBuffer.append(Global.aliyun_href + uploadUrl);
                if(i!=multipartFiles.length-1){
                    stringBuffer.append(",");
                }
            }
        }
        return new UploadSuccessResult(stringBuffer.toString());
    }


    // /article/list?sellerId=3
    @RequestMapping("/list")
    public Result list_new(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<Article> iterable = articleService.list(map, pageNumber, pageSize);
        return new Result(false, Global.do_success, iterable, null);
    }


    //  卖家查看 /article/findone?id=文章ID&sellerId=3
    // 客户查看 /article/findone?id=文章ID&openId=3232
    @RequestMapping("/findone")
    public Result findone(String id,String sellerId,String openId){
        Article article=null;
        if(!StringUtils.isNullOrBlank(sellerId)){
            article= articleService.findOne(Integer.parseInt(id));
        }
        if(!StringUtils.isNullOrBlank(openId)){
            article= articleService.findOne(Integer.parseInt(id));
            article.setViews(article.getViews()+1);
            articleService.update(article);
        }

        return new Result(false, Global.do_success, article, null);
    }

    @RequestMapping("/thumbsup")
    public Result thumbsup(Integer id,String openId){
        Article article=articleService.findOne(id);
        article.setThumsup(article.getThumsup()+1);
        articleService.update(article);
        return new Result(false, Global.do_success, article, null);
    }


    @RequestMapping("/del")
    public Result del(Integer id,String sellerId){
        Article article=articleService.findOne(id);
        article.setActive(false);
        articleService.update(article);
        return new Result(false, Global.do_success, null, null);
    }



}
