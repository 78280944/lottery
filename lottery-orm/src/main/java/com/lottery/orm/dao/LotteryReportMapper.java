package com.lottery.orm.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lottery.orm.dto.InoutReportDto;
import com.lottery.orm.dto.TradeReportDto;
import com.lottery.orm.dto.WinningReportDto;

public interface LotteryReportMapper {
    
    List<WinningReportDto> selectByWinningReport(@Param("startTime")Date startTime,@Param("endTime")Date endTime);
    List<TradeReportDto> selectByTradeReport(@Param("startTime")Date startTime,@Param("endTime")Date endTime, @Param("accountId")Integer accountId);
    List<InoutReportDto> selectByInoutReport(@Param("startTime")Date startTime,@Param("endTime")Date endTime, @Param("accountId")Integer accountId);
}