package com.haoyue.service;

import com.haoyue.pojo.CashTicket;
import com.haoyue.pojo.QCashTicket;
import com.haoyue.repo.CashTicketRepo;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by LiJia on 2017/11/1.
 */
@Service
public class CashTicketService {

    @Autowired
    private CashTicketRepo cashTicketRepo;

    public void save(CashTicket cashTicket,CashTicket ticket) {
        cashTicket.setSellerId(ticket.getSellerId());
        cashTicket.setCash(ticket.getCash());
        cashTicket.setOpenId(ticket.getOpenId());
        cashTicket.setExpireDate(ticket.getExpireDate());
        cashTicket.setFull_cash(ticket.getFull_cash());
        cashTicketRepo.save(cashTicket);
        cashTicket.setCode(String.valueOf(6666+cashTicket.getId()));
        cashTicketRepo.save(cashTicket);
    }

    public void delOlds(String sellerId) {
        cashTicketRepo.deleteBySellerIdAndOpenIdIsNull(sellerId);
    }

    public List<CashTicket> findBySellerIdAndOpenIdIsNull(String sellerId) {
        return cashTicketRepo.findBySellerIdAndOpenIdIsNull(sellerId);
    }

    public void update(CashTicket cashTicket) {
        cashTicketRepo.save(cashTicket);
    }

    public Iterable<CashTicket> find_by_user(String sellerId, String openId) {
        BooleanBuilder bd = new BooleanBuilder();
        QCashTicket ticket=QCashTicket.cashTicket;
        bd.and(ticket.sellerId.eq(sellerId));
        bd.and(ticket.openId.eq(openId));
        bd.and(ticket.isuse.eq(false));
        return cashTicketRepo.findAll(bd.getValue());
    }

    public void del(String id) {
        cashTicketRepo.delete(Integer.parseInt(id));
    }

    public void use(String id){
        CashTicket ticket=cashTicketRepo.findOne(Integer.parseInt(id));
        ticket.setIsuse(true);
        cashTicketRepo.save(ticket);
    }

    public Iterable<CashTicket> list(String sellerId) {
        BooleanBuilder bd = new BooleanBuilder();
        QCashTicket ticket=QCashTicket.cashTicket;
        bd.and(ticket.sellerId.eq(sellerId));
        return cashTicketRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"id"));
    }

    public CashTicket findOne(int id) {
        return cashTicketRepo.findOne(id);
    }

    public CashTicket findByCode(String cashTicketCode) {
        return cashTicketRepo.findByCode(cashTicketCode);
    }
}
