package com.lottery.orm.dto;

import java.math.BigDecimal;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class AccountInfoDto extends BaseAccountDto {
	
	@ApiModelProperty(value = "点数限额", required = true)
	private Double limited;

	@ApiModelProperty(value = "洗码比", required = true)
	private Double ratio;
	
	@ApiModelProperty(value = "账户ID", required = true)
	private Integer accountID;
	
	@ApiModelProperty(value = "账户余额", required = true)
	private BigDecimal accountAmount;
	
	@ApiModelProperty(value = "电话号码", required = true)
	private String phone;
	
	@ApiModelProperty(value = "微信号", required = true)
	private String webchat;
	
	@ApiModelProperty(value = "代理占成", required = true)
	private Double percentage;
	
	@ApiModelProperty(value = "查询权限", required = true)
	private String query;
	
	@ApiModelProperty(value = "管理权限", required = true)
	private String manage;
	
	@ApiModelProperty(value = "账户类型", required = true)
	private String offtype;
	
	
	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getManage() {
		return manage;
	}

	public void setManage(String manage) {
		this.manage = manage;
	}

	public String getOfftype() {
		return offtype;
	}

	public void setOfftype(String offtype) {
		this.offtype = offtype;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebchat() {
		return webchat;
	}

	public void setWebchat(String webchat) {
		this.webchat = webchat;
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

	public BigDecimal getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
	}

}
