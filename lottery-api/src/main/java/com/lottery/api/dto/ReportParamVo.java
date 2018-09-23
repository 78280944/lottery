package com.lottery.api.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class ReportParamVo {

  @ApiModelProperty(value = "帐户ID")
  private Integer accountId;
  
  @ApiModelProperty(value = "开始时间")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date startTime;
  
  @ApiModelProperty(value = "结束时间")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date endTime;


  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }
  
  

}
