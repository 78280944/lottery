package com.lottery.orm.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class OrderDetailDto {
	@ApiModelProperty(value = "投注项ID", required = true)
	private String itemno;
	
	@ApiModelProperty(value = "投注项赔率", required = true)
    private Double itemodds;

	@ApiModelProperty(value = "投注项抽水比例", required = true)
    private Double itembonus;

	@ApiModelProperty(value = "投注项金额", required = true)
    private Double detailamount;

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno == null ? null : itemno.trim();
    }

    public Double getItemodds() {
        return itemodds;
    }

    public void setItemodds(Double itemodds) {
        this.itemodds = itemodds;
    }

    public Double getItembonus() {
        return itembonus;
    }

    public void setItembonus(Double itembonus) {
        this.itembonus = itembonus;
    }

    public Double getDetailamount() {
        return detailamount;
    }

    public void setDetailamount(Double detailamount) {
        this.detailamount = detailamount;
    }

}