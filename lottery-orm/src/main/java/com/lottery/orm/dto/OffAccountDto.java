package com.lottery.orm.dto;

import java.math.BigDecimal;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class OffAccountDto extends SubAccountDto {
	@ApiModelProperty(value = "点数限额", required = true)
	private Double limited;

	@ApiModelProperty(value = "洗码比", required = true)
	private Double ratio;

	@ApiModelProperty(value = "代理占成", required = true)
	private Double percentage;

	@ApiModelProperty(value = "账户ID", required = true)
	private Integer accountID;
	
	@ApiModelProperty(value = "账户余额", required = true)
	private BigDecimal accountAmount;
	
	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}

	public BigDecimal getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
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

}
