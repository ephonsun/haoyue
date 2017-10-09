package com.haoyue.repo;

import com.haoyue.pojo.SuperAdmin;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by LiJia on 2017/9/29.
 */
public interface SuperAdminRepo extends BaseRepo<SuperAdmin,Integer> {
    @Query(nativeQuery = true,value = "select * from super_admin where admin_name=?1 and admin_pass=?2")
    SuperAdmin findByAdmin_nameAndAdmin_pass(String admin_name, String admin_pass);
}
