package com.lottery.orm.bo;

import java.math.BigDecimal;

public class AccountDetail {
    private Integer accountid;

    private Integer userid;

    private String username;

    private Double limited;

    private Double ratio;

    private Double percentage;

    private String state;

    private String supusername;

    private String level;

    private String offtype;

    private BigDecimal money;

    private String attribute1;

    private String attribute2;

    public Integer getAccountid() {
        return accountid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }



    public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Double getLimited() {
        return limited;
    }

    public void setLimited(Double limited) {
        this.limited = limited;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getSupusername() {
        return supusername;
    }

    public void setSupusername(String supusername) {
        this.supusername = supusername == null ? null : supusername.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getOfftype() {
        return offtype;
    }

    public void setOfftype(String offtype) {
        this.offtype = offtype == null ? null : offtype.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1 == null ? null : attribute1.trim();
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2 == null ? null : attribute2.trim();
    }
}