package com.haoyue.web;

import com.haoyue.pojo.ArticleComment;
import com.haoyue.service.ArticleCommentService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Lijia on 2018/5/17.
 * 禾才小程序客户评论文章
 */

@RestController
@RequestMapping("/articlecomment")
public class ArticleCommentController {

    @Autowired
    private ArticleCommentService articleCommentService;

    // /articlecomment/save?openId=123&wxname=微信名称&wxpic=微信头像&articleId=文章ID&comments=评论内容
    @RequestMapping("/save")
    public Result save(ArticleComment articleComment){
        articleComment.setCreateDate(new Date());
        articleCommentService.save(articleComment);
        if(articleComment.getPid()!=null&&articleComment.getPid()!=0){
            ArticleComment parent=articleCommentService.findOne(articleComment.getPid());
            List<ArticleComment> oldchilds= parent.getArticleCommentChilds();
            List<ArticleComment> newchilds=new ArrayList<>();
            newchilds.addAll(oldchilds);
            newchilds.add(articleComment);
            Collections.reverse(newchilds);
            parent.setArticleCommentChilds(newchilds);
            articleCommentService.update(parent);
        }
        return new Result(false, Global.do_success,articleComment,null);
    }


    // /articlecomment/list?articleId=文章ID&sellerId=1
    @RequestMapping("/list")
    public Result list(Integer articleId,String sellerId){
        List<ArticleComment> articleComments= articleCommentService.findByArticleId(articleId);
        return new Result(false, Global.do_success,articleComments,null);
    }


    // /articlecomment/del?id=评论ID&sellerId=1
    @RequestMapping("/del")
    public Result del(Integer id,String sellerId){
        articleCommentService.del(id);
        return new Result(false, Global.do_success,null,null);
    }


}
