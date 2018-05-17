package com.haoyue.repo;

import com.haoyue.pojo.ArticleComment;

import java.util.List;

/**
 * Created by Lijia on 2018/5/17.
 */
public interface ArticleCommentRepo extends BaseRepo<ArticleComment,Integer> {
    List<ArticleComment> findByArticleId(Integer articleId);
}
