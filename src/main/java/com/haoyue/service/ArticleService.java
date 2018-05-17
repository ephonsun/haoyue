package com.haoyue.service;

import com.haoyue.pojo.Article;
import com.haoyue.pojo.QArticle;
import com.haoyue.repo.ArticleRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Lijia on 2018/5/17.
 */

@Service
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    public void save(Article article) {
        articleRepo.save(article);
    }

    public Iterable<Article> list(Map<String, String> map, int pageNumber, int pageSize) {

        QArticle article=QArticle.article;
        BooleanBuilder bd=new BooleanBuilder();
        bd.and(article.active.eq(true));
        for(String key:map.keySet()){
            String value=map.get(key);
            if(!StringUtils.isNullOrBlank(value)){
                if(key.equals("sellerId")){
                    bd.and(article.sellerId.eq(value));
                }
            }

        }

        return articleRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));

    }

    public Article findOne(Integer id) {
        return articleRepo.findOne(id);
    }

    public void update(Article article) {
        articleRepo.save(article);
    }
}
