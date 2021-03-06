package com.lottery.orm.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class BaseAccountDto {
	@ApiModelProperty(value = "(流水号)用户ID", required = true)
	private Integer userid;

	@ApiModelProperty(value = "用户名", required = true)
	private String username;

	@ApiModelProperty(value = "别名", required = true)
	private String ausername;

	@ApiModelProperty(value = "登陆密码", required = true)
	private String password;

	@ApiModelProperty(value = "状态,0:冻结，1:正常", required = true)
	private String state;

	@ApiModelProperty(value = "管理账户", required = true)
	private String supusername;

	@ApiModelProperty(value = "管理账户级别，0：超级管理员，1：一级代理，2：二级代理，3：三级代理", required = true)
	private String level;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSupusername() {
		return supusername;
	}

	public void setSupusername(String supusername) {
		this.supusername = supusername;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		this.username = username;
	}

	public String getAusername() {
		return ausername;
	}

	public void setAusername(String ausername) {
		this.ausername = ausername;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
