package com.haoyue.web;

import com.haoyue.pojo.CashTicket;
import com.haoyue.service.CashTicketService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by LiJia on 2017/11/1.
 */
@RestController
@RequestMapping("/cashticket")
public class CashTicketController {

    @Autowired
    private CashTicketService cashTicketService;

    // /cashticket/save?count=抵用券数量&sellerId=1&cash=[单张券抵多少人民币，整数，以元为单位]&expireDate=过期时间
    @RequestMapping("/save")
    public Result save(int count, CashTicket ticket){
        List<CashTicket> cashTicketList=new ArrayList<>();
        Date date=new Date();
        //删除旧的抵用券
        cashTicketService.delOlds(ticket.getSellerId());
        while (count>0){
            count--;
            CashTicket cashTicket=new CashTicket();
            cashTicket.setCreateDate(date);
            cashTicketService.save(cashTicket,ticket);
            cashTicketList.add(cashTicket);
        }
        return new Result(false, Global.do_success,cashTicketList,null);
    }

    // /cashticket/get?openId=1&sellerId=1&wxname=微信昵称
    @RequestMapping("/get")
    public Result get(String openId,String sellerId,String wxname){
        List<CashTicket> cashTicketList= cashTicketService.findBySellerIdAndOpenIdIsNull(sellerId);
        if (cashTicketList==null||cashTicketList.size()==0){
            return new Result(true, Global.no_cashticket,null,null);
        }
        CashTicket cashTicket=cashTicketList.get(0);
        Iterable<CashTicket> cashTickets=cashTicketService.find_by_user(sellerId,openId);
        Iterator<CashTicket> iterator=cashTickets.iterator();
        while (iterator.hasNext()){
            if (iterator.next().getCreateDate().equals(cashTicket.getCreateDate())){
                return new Result(true,Global.get_ticket_alerady,null,null);
            }
        }
        cashTicket.setOpenId(openId);
        cashTicket.setWxname(wxname);
        cashTicketService.update(cashTicket);
        return new Result(false, Global.do_success,cashTicket,null);
    }

    // /cashticket/find_by_user?openId=1&sellerId=1
    @RequestMapping("/find_by_user")
    public Result find_by_user(String openId,String sellerId){
        Iterable<CashTicket> cashTicketList=cashTicketService.find_by_user(sellerId,openId);
        return new Result(false,Global.do_success,cashTicketList,null);
    }

    //   /cashticket/del?id=抵用券Id&sellerId=1
    @RequestMapping("/del")
    public Result del(String id,String sellerId){
        cashTicketService.del(id);
        return new Result(false,Global.do_success,null,null);
    }
  //   /cashticket/list?sellerId=1
    @RequestMapping("/list")
    public Result list(String sellerId){
        Iterable<CashTicket> cashTicketList=cashTicketService.list(sellerId);
        return new Result(false,Global.do_success,cashTicketList,null);
    }

}
