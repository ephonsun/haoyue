package com.haoyue.service;

import com.haoyue.pojo.ArticleComment;
import com.haoyue.repo.ArticleCommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lijia on 2018/5/17.
 */


@Service
public class ArticleCommentService {

    @Autowired
    private ArticleCommentRepo articleCommentRepo;

    public void save(ArticleComment articleComment) {
        articleCommentRepo.save(articleComment);
    }

    public ArticleComment findOne(Integer pid) {
        return articleCommentRepo.findOne(pid);
    }

    public void update(ArticleComment parent) {
        articleCommentRepo.save(parent);
    }

    public List<ArticleComment> findByArticleId(Integer articleId) {
        return articleCommentRepo.findByArticleId(articleId);
    }

    public void del(Integer id) {
        articleCommentRepo.delete(id);
    }
}
