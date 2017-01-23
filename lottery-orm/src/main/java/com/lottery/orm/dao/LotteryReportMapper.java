package com.lottery.orm.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lottery.orm.dto.HistoryOrderDto;
import com.lottery.orm.dto.InoutReportDto;
import com.lottery.orm.dto.TradeReportDto;
import com.lottery.orm.dto.WinningReportDto;

public interface LotteryReportMapper {
    
    List<WinningReportDto> selectByWinningReport(@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("beginRow")Integer beginRow,@Param("pageSize")Integer pageSize);
    
    List<TradeReportDto> selectByTradeReport(@Param("startTime")Date startTime,@Param("endTime")Date endTime, @Param("accountId")Integer accountId,@Param("beginRow")Integer beginRow,@Param("pageSize")Integer pageSize);
    
    List<InoutReportDto> selectByInoutReport(@Param("startTime")Date startTime,@Param("endTime")Date endTime, @Param("accountId")Integer accountId,@Param("beginRow")Integer beginRow,@Param("pageSize")Integer pageSize);
 
    List<HistoryOrderDto> selectByCurRoundOrder(@Param("roundId")Integer roundId, @Param("accountId")Integer accountId);
    
    List<HistoryOrderDto> selectByHistoryOrder(@Param("startTime")Date startTime,@Param("endTime")Date endTime, @Param("accountId")Integer accountId,@Param("beginRow")Integer beginRow,@Param("pageSize")Integer pageSize);
    
}