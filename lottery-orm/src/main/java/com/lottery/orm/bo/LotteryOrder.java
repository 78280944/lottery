package com.lottery.orm.bo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LotteryOrder {
    private Integer orderid;

    private Integer roundid;

    private Integer accountid;

    private Double orderamount;

    private Double commisionamount;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date ordertime;

    private Double prizeamount;

    private Double actualamount;

    private Double returnamount;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date prizetime;

    private BigDecimal accountamount;
    
    List<LotteryOrderDetail> orderDetailList;

    public Integer getOrderid() {
	return orderid;
    }

    public void setOrderid(Integer orderid) {
	this.orderid = orderid;
    }

    public Integer getRoundid() {
	return roundid;
    }

    public void setRoundid(Integer roundid) {
	this.roundid = roundid;
    }

    public Integer getAccountid() {
	return accountid;
    }

    public void setAccountid(Integer accountid) {
	this.accountid = accountid;
    }

    public Double getOrderamount() {
	return orderamount;
    }

    public void setOrderamount(Double orderamount) {
	this.orderamount = orderamount;
    }

    public Double getCommisionamount() {
	return commisionamount;
    }

    public void setCommisionamount(Double commisionamount) {
	this.commisionamount = commisionamount;
    }

    public Date getOrdertime() {
	return ordertime;
    }

    public void setOrdertime(Date ordertime) {
	this.ordertime = ordertime;
    }

    public Double getPrizeamount() {
	return prizeamount;
    }

    public void setPrizeamount(Double prizeamount) {
	this.prizeamount = prizeamount;
    }

    public Double getActualamount() {
	return actualamount;
    }

    public void setActualamount(Double actualamount) {
	this.actualamount = actualamount;
    }

    public Double getReturnamount() {
	return returnamount;
    }

    public void setReturnamount(Double returnamount) {
	this.returnamount = returnamount;
    }

    public Date getPrizetime() {
	return prizetime;
    }

    public void setPrizetime(Date prizetime) {
	this.prizetime = prizetime;
    }

    public BigDecimal getAccountamount() {
	return accountamount;
    }

    public void setAccountamount(BigDecimal accountamount) {
	this.accountamount = accountamount;
    }
    
    public List<LotteryOrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<LotteryOrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
    
}