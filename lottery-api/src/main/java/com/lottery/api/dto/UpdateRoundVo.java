package com.lottery.api.dto;

import org.hibernate.validator.constraints.NotBlank;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class UpdateRoundVo {

	@ApiModelProperty(value = "开奖日期,格式为2017-01-01")
	@NotBlank(message = "开奖日期不能为空")
    private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
}
