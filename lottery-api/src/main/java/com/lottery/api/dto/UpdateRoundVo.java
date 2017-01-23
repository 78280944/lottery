package com.lottery.api.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class UpdateRoundVo {

    @ApiModelProperty(value = "游戏ID")
    @NotNull(message = "游戏ID不能为空")
    @Min(value=0, message = "游戏ID格式不正确")
    private Integer roundId;

    @ApiModelProperty(value = "开奖结果, 比如广西快乐十分开奖结果:10,07,04,16,11")
    @NotBlank(message = "开奖结果不能为空")
    private String resultStr;

    public Integer getRoundId() {
	return roundId;
    }

    public void setRoundId(Integer roundId) {
	this.roundId = roundId;
    }

    public String getResultStr() {
	return resultStr;
    }

    public void setResultStr(String resultStr) {
	this.resultStr = resultStr;
    }

}
