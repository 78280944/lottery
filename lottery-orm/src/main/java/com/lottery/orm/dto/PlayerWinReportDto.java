package com.lottery.orm.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class PlayerWinReportDto {
	@ApiModelProperty(value = "账户ID", required = true)
	private Integer accountId;
	
	@ApiModelProperty(value = "用户名", required = true)
	private String userName;
	
	@ApiModelProperty(value = "所属代理", required = true)
	private String supUserName;
	
	@ApiModelProperty(value = "游戏期次", required = true)
	private String lotteryTerm;
	
	@ApiModelProperty(value = "订单号", required = true)
	private Integer orderId;
	
	@ApiModelProperty(value = "下注时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTime;

	@ApiModelProperty(value = "投注金额", required = true)
	private Double orderAmount;

	@ApiModelProperty(value = "派彩", required = true)
	private Double actualAmount;
	
	@ApiModelProperty(value = "返利", required = true)
	private Double returnAmount;

	@ApiModelProperty(value = "输赢", required = true)
	private Double winAmount;
	
	@ApiModelProperty(value = "公司损益", required = true)
	private Double systemAmount;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSupUserName() {
		return supUserName;
	}

	public void setSupUserName(String supUserName) {
		this.supUserName = supUserName;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getLotteryTerm() {
		return lotteryTerm;
	}

	public void setLotteryTerm(String lotteryTerm) {
		this.lotteryTerm = lotteryTerm;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Double getWinAmount() {
		return winAmount;
	}

	public void setWinAmount(Double winAmount) {
		this.winAmount = winAmount;
	}

	public Double getSystemAmount() {
		return systemAmount;
	}

	public void setSystemAmount(Double systemAmount) {
		this.systemAmount = systemAmount;
	}

}
