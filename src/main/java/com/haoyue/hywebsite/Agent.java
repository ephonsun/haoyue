package com.haoyue.hywebsite;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2018/1/12.
 */
@Entity
@Table(name = "website_agent")
public class Agent {
    @Id
    @GeneratedValue
    private Integer id;
    private String companyName;//公司名
    private String companyProvince;//公司省份
    private double floatMoney;//流动资金
    private String preferCity;//代理城市
    private String manageScope;//经营范围
    private int employeeNum;//员工数
    private String agentPast;//过往代理
    private String CustomerSourceDescribe;//客户资源描述
    private String planEnjoys;//从事业务人员
    private int salesNum;//销售人数
    private String manager;//负责人
    private String managerPhone;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyProvince() {
        return companyProvince;
    }

    public void setCompanyProvince(String companyProvince) {
        this.companyProvince = companyProvince;
    }

    public double getFloatMoney() {
        return floatMoney;
    }

    public void setFloatMoney(double floatMoney) {
        this.floatMoney = floatMoney;
    }

    public String getPreferCity() {
        return preferCity;
    }

    public void setPreferCity(String preferCity) {
        this.preferCity = preferCity;
    }

    public String getManageScope() {
        return manageScope;
    }

    public void setManageScope(String manageScope) {
        this.manageScope = manageScope;
    }

    public int getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(int employeeNum) {
        this.employeeNum = employeeNum;
    }

    public String getAgentPast() {
        return agentPast;
    }

    public void setAgentPast(String agentPast) {
        this.agentPast = agentPast;
    }

    public String getCustomerSourceDescribe() {
        return CustomerSourceDescribe;
    }

    public void setCustomerSourceDescribe(String customerSourceDescribe) {
        CustomerSourceDescribe = customerSourceDescribe;
    }

    public String getPlanEnjoys() {
        return planEnjoys;
    }

    public void setPlanEnjoys(String planEnjoys) {
        this.planEnjoys = planEnjoys;
    }

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
