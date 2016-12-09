package com.lottery.api.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class TradeParamVo {

    @ApiModelProperty(value = "款项ID", required = true)
    @NotNull(message = "款项ID不能为空")
    @Min(value=0, message = "款项ID格式不正确")
    private Integer accountId;

    @ApiModelProperty(value = "金额", required = true)
    @NotNull(message = "金额不能为空")
    @DecimalMin(value="0.01", message = "金额必须大于零")
    private Double tradeAmount;

    public Integer getAccountId() {
	return accountId;
    }

    public void setAccountId(Integer accountId) {
	this.accountId = accountId;
    }

    public Double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

}
