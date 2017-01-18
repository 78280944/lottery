package com.lottery.orm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.bo.LotteryItem;
import com.lottery.orm.bo.LotteryOrder;
import com.lottery.orm.bo.LotteryRound;

public interface CustomLotteryMapper {
    
    List<Map<String, String>> selectOrderForCheck(@Param("roundId")Integer roundId);
    
    List<AccountDetail> selectAccountBySupUserName(@Param("supUserName")String supUserName);
    
    List<LotteryItem> selectItemByLotteryType(@Param("lotteryType")String lotteryType);
    
    List<LotteryOrder> selectOrderByRoundId(@Param("roundId")Integer roundId);
    
    Integer selectRoundIdByStatus(@Param("lotteryType")String lotteryType, @Param("roundStatus")String roundStatus);
    
    LotteryRound selectRoundByRoundId(@Param("roundId")Integer roundId);
    
    LotteryRound selectHistoryOrder(@Param("roundId")Integer roundId);
}