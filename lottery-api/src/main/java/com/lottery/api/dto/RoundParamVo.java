package com.lottery.api.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class RoundParamVo {

    @ApiModelProperty(value = "游戏期次")
    @NotBlank(message = "游戏期次不能为空")
    private String lotteryTerm;

    @ApiModelProperty(value = "游戏开始时间")
    @NotNull(message = "游戏开始时间不能为空")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date startTime;

    public String getLotteryTerm() {
	return lotteryTerm;
    }

    public void setLotteryTerm(String lotteryTerm) {
	this.lotteryTerm = lotteryTerm;
    }

    public Date getStartTime() {
	return startTime;
    }

    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

}
