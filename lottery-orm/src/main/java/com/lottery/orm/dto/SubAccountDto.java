package com.lottery.orm.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class SubAccountDto extends BaseAccountDto {

	@ApiModelProperty(value = "查询权限", required = true)
	private String query;

	@ApiModelProperty(value = "管理权限", required = true)
	private String manage;

	@ApiModelProperty(value = "账户类型：0超级账户，1代理账户，2子账户类型，3会员账户", required = true)
	private String offtype;

	public String getOfftype() {
		return offtype;
	}

	public void setOfftype(String offtype) {
		this.offtype = offtype;
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

}
