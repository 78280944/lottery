package com.lottery.api.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class SubAccountInfoVo extends BaseAccountInfoVo{
	
	@ApiModelProperty(value = "查询权限,Y1(账号管理)，Y2(子账户管理),Y3(输赢报表),Y4(交易记录报表),Y5(点数出入报表)")
	private String query;
	@ApiModelProperty(value = "管理权限,M1(账号管理)，M2(子账户管理),M3(输赢报表),M4(交易记录报表),M5(点数出入报表)")
	private String manage;
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
