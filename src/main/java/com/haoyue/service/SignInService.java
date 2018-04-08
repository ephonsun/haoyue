package com.haoyue.service;

import com.haoyue.pojo.QSignIn;
import com.haoyue.pojo.SignIn;
import com.haoyue.repo.SignInRepo;
import com.haoyue.untils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Lijia on 2018/4/8.
 */
@Service
public class SignInService {

    @Autowired
    private SignInRepo signInRepo;

    public void save(SignIn signin) {
        signInRepo.save(signin);
    }

    public boolean findIsSignIn(SignIn signin) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        signin.setCreateDate(calendar.getTime());
        SignIn signIn = signInRepo.findIsSignIn(signin.getOpenId(), signin.getSellerId(), signin.getCreateDate());
        return signIn==null?true:false;
    }

    public List<SignIn> findByOpenIdAndSellerIdAndActive(String openId,String sellerId,boolean b) {
        return signInRepo.findByOpenIdAndSellerIdAndActive(openId,sellerId,b);
    }

    public void update(SignIn signIn) {
        signInRepo.save(signIn);
    }

    public Iterable<SignIn> list(Map<String, String> map, int pageNumber, int pageSize) {
        QSignIn signIn = QSignIn.signIn;
        BooleanBuilder bd = new BooleanBuilder();

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {

                if(name.equals("sellerId")){
                    bd.and(signIn.sellerId.eq(value));
                }
                else if(name.equals("openId")){
                    bd.and(signIn.openId.eq(value));
                }
                else if(name.equals("active")){
                    bd.and(signIn.active.eq(Boolean.valueOf(value)));
                }

            }
        }
        return signInRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,new String[]{"id"})));
    }
}
