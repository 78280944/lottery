package com.lottery.orm.result;

import com.lottery.orm.bo.LotteryOrder;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class OrderResult extends BaseRestResult {

	@ApiModelProperty(value = "投注信息", required = true)
	private LotteryOrder data;

	public void success(LotteryOrder data) {
		success();
		this.data = data;
	}

	public LotteryOrder getData() {
		return data;
	}

	public void setData(LotteryOrder data) {
		this.data = data;
	}

}
