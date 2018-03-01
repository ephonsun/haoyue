package com.haoyue.service;

import com.haoyue.pojo.Comment;
import com.haoyue.pojo.Order;
import com.haoyue.pojo.QComment;
import com.haoyue.repo.CommentRepo;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/8/24.
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;


    public Comment findOne(Integer id) {
        return commentRepo.findOne(id);
    }


    public Comment save(Comment comment) {
        return commentRepo.save(comment);
    }

    public List<Comment> findByPid(String pid) {
        return commentRepo.findByPid(pid);
    }

    public List<Comment> findBySellerId(String sellerId) {
        return commentRepo.findBySellerId(Integer.parseInt(sellerId));
    }

    public Iterable<Comment> list(Map<String, String> map, int pageNumber, int pageSize) {
        QComment comment = QComment.comment;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(comment.sellerId.eq(Integer.parseInt(map.get("sellerId"))));
        return commentRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));
    }

    public void updateWxname(String wxname, String wxpic, String openId, String sellerId) {
        commentRepo.updateWxname(wxname,wxpic,openId,sellerId);
    }
}
