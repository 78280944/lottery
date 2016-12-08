package com.lottery.orm.result;

import java.util.List;

import com.lottery.orm.dto.WinningReportDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * POJO class for rest process result.
 * 
 */
public class WinningReportResult extends BaseRestResult {
    
    @ApiModelProperty(value = "输赢报表数据", required = true)
    private List<WinningReportDto> data;

    public void success(List<WinningReportDto> data) {
	success();
	this.data = data;
    }

    public List<WinningReportDto> getData() {
        return data;
    }

    public void setData(List<WinningReportDto> data) {
        this.data = data;
    }

}
