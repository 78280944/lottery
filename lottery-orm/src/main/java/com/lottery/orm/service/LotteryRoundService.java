package com.lottery.orm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lottery.orm.bo.LotteryItem;
import com.lottery.orm.bo.LotteryOrder;
import com.lottery.orm.bo.LotteryRound;
import com.lottery.orm.bo.LotteryRoundItem;
import com.lottery.orm.dao.CustomLotteryMapper;
import com.lottery.orm.dao.LotteryRoundItemMapper;
import com.lottery.orm.dao.LotteryRoundMapper;
import com.lottery.orm.util.EnumType;

@Service
@Transactional
public class LotteryRoundService {

    @Autowired
    private LotteryRoundMapper lotteryRoundMapper;

    @Autowired
    private LotteryRoundItemMapper roundItemMapper;

    @Autowired
    private CustomLotteryMapper customLotteryMapper;
    
    @Autowired
    private LotteryOrderService lotteryOrderService;

    // 添加下注单
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean addLotteryRound(LotteryRound round) {
	List<LotteryItem> itemList = customLotteryMapper.selectItemByLotteryType(round.getLotterytype());
	List<LotteryRoundItem> roundItemList = new ArrayList<LotteryRoundItem>();
	lotteryRoundMapper.insertSelective(round);
	if (round.getRoundid() > 0) {
	    for (LotteryItem item : itemList) {
		LotteryRoundItem roundItem = new LotteryRoundItem();
		roundItem.setRoundid(round.getRoundid());
		roundItem.setItemno(item.getItemno());
		roundItem.setItemscale(item.getItemscale());
		roundItem.setItemodds(item.getItemodds());
		roundItem.setItembonus(item.getItembonus());
		roundItemMapper.insertSelective(roundItem);
		roundItemList.add(roundItem);
	    }
	    round.setRoundItemList(roundItemList);
	    return true;
	}
	return false;
    }

    // 添加下注单
    public boolean endLotteryRound(LotteryRound round) {
	round.setRoundstatus(EnumType.RoundStatus.End.ID);
	round.setEndtime(new Date());
	if (lotteryRoundMapper.updateByPrimaryKeySelective(round) > 0) {
	    List<LotteryItem> itemList = customLotteryMapper.selectItemByLotteryType(EnumType.LotteryType.CornSeed.ID);
	    List<LotteryOrder> orderList = customLotteryMapper.selectOrderByRoundId(round.getRoundid());
	    for (LotteryOrder order : orderList) {
		lotteryOrderService.updateOrderByRound(round, order, itemList);
	    }
	    return true;
	}
	return false;
    }

}
