package com.lottery.orm.bo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class LotteryRound {
    @ApiModelProperty(value = "游戏ID", required = true)
    private Integer roundid;
    
    @ApiModelProperty(value = "游戏类型：01玉米籽", required = true)
    private String lotterytype;
    
    @ApiModelProperty(value = "期次", required = true)
    private String lotteryterm;
    
    @ApiModelProperty(value = "开出之号码")
    private String resultstr;
    
    @ApiModelProperty(value = "游戏开始时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date starttime;
    
    @ApiModelProperty(value = "游戏开奖时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date endtime;

    @ApiModelProperty(value = "游戏状态：Open开盘中，Close已封盘，End已结束")
    private String roundstatus;
    
    @ApiModelProperty(value = "该期游戏投注项信息")
    private List<LotteryRoundItem> roundItemList;

    public Integer getRoundid() {
        return roundid;
    }

    public void setRoundid(Integer roundid) {
        this.roundid = roundid;
    }

    public String getLotterytype() {
        return lotterytype;
    }

    public void setLotterytype(String lotterytype) {
        this.lotterytype = lotterytype == null ? null : lotterytype.trim();
    }

    public String getLotteryterm() {
        return lotteryterm;
    }

    public void setLotteryterm(String lotteryterm) {
        this.lotteryterm = lotteryterm == null ? null : lotteryterm.trim();
    }

    public String getResultstr() {
        return resultstr;
    }

    public void setResultstr(String resultstr) {
        this.resultstr = resultstr == null ? null : resultstr.trim();
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getRoundstatus() {
        return roundstatus;
    }

    public void setRoundstatus(String roundstatus) {
        this.roundstatus = roundstatus == null ? null : roundstatus.trim();
    }

    public List<LotteryRoundItem> getRoundItemList() {
        return roundItemList;
    }

    public void setRoundItemList(List<LotteryRoundItem> roundItemList) {
        this.roundItemList = roundItemList;
    }
    
    
}