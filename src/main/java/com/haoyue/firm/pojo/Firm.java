package com.haoyue.firm.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by LiJia on 2017/9/27.
 * 企业宣传小程序 与 电商小程序合并在一起 请注意区分 该包下将涵盖所有的企业宣传文件
 */
@Entity
@Table(name = "firms")
public class Firm {

    @Id
    @GeneratedValue
    private Integer id;
    private String firm_name;
    private String firm_pass;

    public String getFirm_name() {
        return firm_name;
    }

    public void setFirm_name(String firm_name) {
        this.firm_name = firm_name;
    }

    public String getFirm_pass() {
        return firm_pass;
    }

    public void setFirm_pass(String firm_pass) {
        this.firm_pass = firm_pass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
