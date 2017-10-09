package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.Comment;
import com.haoyue.pojo.Seller;
import com.haoyue.service.CommentService;
import com.haoyue.service.SellerService;
import com.haoyue.untils.Global;
import com.haoyue.untils.OSSClientUtil;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by LiJia on 2017/8/24.
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private SellerService sellerService;


    @RequestMapping("/reply")
    public Result reply(Integer id, String token , String message){
        if (message.length()>250){
            return new Result(true, Global.message_tolong,token);
        }
        Comment comment= commentService.findOne(id);
        if (comment.getSellerId()!=Integer.parseInt(token)){
            return new Result(true, Global.have_no_right,token);
        }
        comment.setReversion(message);
        commentService.reply(comment);
        return new Result(false, Global.do_success,token);
    }

    @RequestMapping("/save")
    public Result save(String openId,Comment comment){
        return new Result(false,Global.do_success,commentService.save(openId,comment),null);
    }

    @RequestMapping("/uploadPics")
    public Result uploadPics(MultipartFile[] multipartFiles,Integer sellerId) throws MyException {

        Seller seller=sellerService.findOne(sellerId);
        StringBuffer stringBuffer=new StringBuffer();
        for (MultipartFile multipartFile:multipartFiles) {
            int kb=(int)(multipartFile.getSize()/1024);
            seller.setUploadFileSize(seller.getUploadFileSize()+kb);
            OSSClientUtil ossClientUtil = new OSSClientUtil();
            String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
            stringBuffer.append(uploadUrl);
            stringBuffer.append(",");
        }
        sellerService.update2(seller);
        return new Result(false,Global.do_success,stringBuffer.toString(),null);
    }

}
