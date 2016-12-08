package com.lottery.orm.dto;

import java.math.BigDecimal;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class WinningReportDto {
	@ApiModelProperty(value = "账户ID", required = true)
	private Integer accountId;

	@ApiModelProperty(value = "交易次数", required = true)
	private Integer tradeCount;

	@ApiModelProperty(value = "投注金额", required = true)
	private BigDecimal orderAmount;

	@ApiModelProperty(value = "输赢金额", required = true)
	private BigDecimal actualAmount;

	@ApiModelProperty(value = "洗码量", required = true)
	private BigDecimal returnAmount;

	@ApiModelProperty(value = "代理总金额", required = true)
	private BigDecimal commisionAmount;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getTradeCount() {
		return tradeCount;
	}

	public void setTradeCount(Integer tradeCount) {
		this.tradeCount = tradeCount;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public BigDecimal getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(BigDecimal returnAmount) {
		this.returnAmount = returnAmount;
	}

	public BigDecimal getCommisionAmount() {
		return commisionAmount;
	}

	public void setCommisionAmount(BigDecimal commisionAmount) {
		this.commisionAmount = commisionAmount;
	}

}
