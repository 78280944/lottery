package com.lottery.orm.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.bo.LotteryItem;
import com.lottery.orm.bo.LotteryOrder;
import com.lottery.orm.bo.LotteryOrderDetail;
import com.lottery.orm.bo.LotteryRound;
import com.lottery.orm.bo.LotteryRoundItem;
import com.lottery.orm.bo.TradeInfo;
import com.lottery.orm.dao.AccountDetailMapper;
import com.lottery.orm.dao.CustomLotteryMapper;
import com.lottery.orm.dao.LotteryOrderDetailMapper;
import com.lottery.orm.dao.LotteryOrderMapper;
import com.lottery.orm.dao.TradeInfoMapper;
import com.lottery.orm.util.EnumType;

@Service
@Transactional
public class LotteryOrderService {

	@Autowired
	private CustomLotteryMapper customLotteryMapper;

	@Autowired
	private LotteryOrderMapper lotteryOrderMapper;

	@Autowired
	private AccountDetailMapper accountDetailMapper;

	@Autowired
	private TradeInfoMapper tradeInfoMapper;

	@Autowired
	private LotteryOrderDetailMapper lotteryOrderDetailMapper;

	// 添加投注单
	public LotteryOrder addLotteryOrder(LotteryOrder order) {
		order.setOrdertime(new Date());
		lotteryOrderMapper.insertSelective(order);
		Double orderAmount = 0.0;
		LotteryRound round = customLotteryMapper.selectRoundByRoundId(order.getRoundid());
		List<LotteryRoundItem> itemList = round.getRoundItemList();
		for (LotteryOrderDetail orderDetail : order.getOrderDetailList()) {
			LotteryRoundItem tempItem = new LotteryRoundItem();
			tempItem.setItemno(orderDetail.getItemno());
			LotteryRoundItem roundItem = itemList.get(itemList.indexOf(tempItem));

			orderDetail.setItemodds(roundItem.getItemodds());
			orderDetail.setItembonus(roundItem.getItembonus());
			orderDetail.setOrderid(order.getOrderid());
			lotteryOrderDetailMapper.insertSelective(orderDetail);
			orderAmount += orderDetail.getDetailamount();
		}
		order.setOrderamount(orderAmount);
		lotteryOrderMapper.updateByPrimaryKeySelective(order);
		return order;
	}

	// 根据中奖结果更新投注单结果
	public boolean updateOrderByRound(LotteryRound round, LotteryOrder order, List<LotteryItem> itemList) {
		Double windOrder = 0.0;
		Double lossOrder = 0.0;
		Double totalPrize = 0.0;
		Double totalActual = 0.0;
		Double totalReturn = 0.0;
		AccountDetail account = accountDetailMapper.selectByPrimaryKey(order.getAccountid());
		for (LotteryOrderDetail detail : order.getOrderDetailList()) {// 投注细项汇总
			LotteryItem tempItem = new LotteryItem();
			tempItem.setItemno(detail.getItemno());
			LotteryItem item = itemList.get(itemList.indexOf(tempItem));
			if (isWinPrize(round, item)) {
				Double prizeAmount = detail.getDetailamount() * detail.getItemodds();
				Double actualAmount = prizeAmount - prizeAmount * detail.getItembonus();
				Double returnAmount = detail.getDetailamount() * account.getRatio();
				detail.setPrizeamount(prizeAmount);
				detail.setActualamount(actualAmount);
				detail.setReturnamount(returnAmount);
				windOrder += detail.getDetailamount();
				totalPrize += prizeAmount;
				totalActual += actualAmount;
				totalReturn += returnAmount;
				lotteryOrderDetailMapper.updateByPrimaryKeySelective(detail);
			} else {
				lossOrder += detail.getDetailamount();
			}
		}
		order.setPrizeamount(totalPrize);
		order.setActualamount(totalActual);
		order.setReturnamount(totalReturn);
		order.setPrizetime(new Date());

		List<AccountDetail> parentAccounts = customLotteryMapper.selectAccountBySupUserName(account.getSupusername());
		AccountDetail systemAccount = parentAccounts.get(0);// 系统账户
		AccountDetail firstAccount = parentAccounts.get(1);// 一级代理账户
		Double prizeCommision = 0.0;// 庄家从奖金中抽取的佣金
		Double orderCommision = 0.0;// 庄家从玩家的输单中抽取的佣金
		Double systemCommision = 0.0;// 系统平台抽取佣金
		if (windOrder > 0) {//// 玩家赢的投注项

			addTradeInfo(account, order, totalActual, EnumType.RalativeType.Prize.ID);
			addTradeInfo(account, order, totalReturn, EnumType.RalativeType.Return.ID);

			addTradeInfo(firstAccount, order, totalPrize, EnumType.RalativeType.Pay.ID);// 庄家输钱了

			prizeCommision = (totalPrize - totalActual) * 0.8;
			systemCommision = (totalPrize - totalActual) * 0.06;
			Double extraCommision = (totalPrize - totalActual) * 0.14 - totalReturn;// 玩家额外佣金，即抽水的14%减去抽水返利之后，还有剩余的话作为庄家佣金
			prizeCommision = extraCommision > 0 ? prizeCommision + extraCommision : prizeCommision;

		}

		if (lossOrder > 0) {// 玩家输的投注项
			addTradeInfo(account, order, lossOrder, EnumType.RalativeType.Order.ID);// 玩家输了投注本金
			orderCommision += lossOrder * (0.8 + 0.14);// 提成
			systemCommision += lossOrder * 0.06;
		}
		order.setCommisionamount(orderCommision + prizeCommision + systemCommision);
		lotteryOrderMapper.updateByPrimaryKeySelective(order);
		addTradeInfo(firstAccount, order, prizeCommision + orderCommision, EnumType.RalativeType.Commision.ID);// 庄家抽取的总佣金
		addTradeInfo(systemAccount, order, systemCommision, EnumType.RalativeType.Commision.ID);// 系统平台抽取佣金
		return true;
	}

	// 新增一级代理佣金款项
	private void addTradeInfo(AccountDetail account, LotteryOrder order, Double tradeAmount, String relativeType) {
		BigDecimal accountAmount;
		if (relativeType.equals(EnumType.RalativeType.Order.ID) || relativeType.equals(EnumType.RalativeType.Pay.ID)) {
			tradeAmount = 0.0 - tradeAmount;
			// accountAmount = account.getMoney().subtract(new
			// BigDecimal(order.getOrderamount()));
		}
		accountAmount = account.getMoney().add(new BigDecimal(tradeAmount));
		TradeInfo tradeInfo = new TradeInfo();
		tradeInfo.setAccountid(account.getAccountid());
		tradeInfo.setTradetype(EnumType.TradeType.Trade.ID);
		tradeInfo.setRelativetype(relativeType);
		tradeInfo.setRelativeid(order.getOrderid());
		tradeInfo.setTradeamount(tradeAmount);
		tradeInfo.setAccountamount(accountAmount);
		tradeInfo.setInputtime(new Date());
		tradeInfoMapper.insertSelective(tradeInfo);

		account.setMoney(accountAmount);
		accountDetailMapper.updateByPrimaryKeySelective(account);

		if (order.getAccountid() == account.getAccountid()) {// 更新玩家下单记录的账户余额
			order.setAccountamount(accountAmount);
			lotteryOrderMapper.updateByPrimaryKeySelective(order);
		}
	}

	// 根据中奖结果判断是否中奖
	private boolean isWinPrize(LotteryRound round, LotteryItem item) {
		String[] orderNumbers = item.getItemname().split(",");
		int prizeNumber = Integer.parseInt(round.getResultstr());
		int cronNumber = prizeNumber % 4 == 0 ? 4 : prizeNumber % 4;

		// String[] prizeNumbers = round.getResultstr().split(",");
		if (item.getItemtype().equals(EnumType.ItemType.Type_01.ID)) {
			for (int i = 0; i < orderNumbers.length; i++) {
				if (orderNumbers[i].equals(String.valueOf(cronNumber))) {
					return true;
				}
			}
		} else if (item.getItemtype().equals(EnumType.ItemType.Type_02.ID)) {
			for (int i = 0; i < orderNumbers.length; i++) {
				if (orderNumbers[i].equals(String.valueOf(prizeNumber))) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * // 中奖时添加款项记录 private LotteryOrder addTradeInfoByOrder(LotteryOrder order)
	 * { AccountDetail account =
	 * accountDetailMapper.selectByPrimaryKey(order.getAccountid());
	 * lotteryOrderMapper.updateByPrimaryKeySelective(order); BigDecimal
	 * accountAmount = account.getMoney().add(new
	 * BigDecimal(order.getActualamount())); TradeInfo tradeInfo = new
	 * TradeInfo(); tradeInfo.setTradetype(EnumType.TradeType.Order.ID);
	 * tradeInfo.setRelativetype(EnumType.RalativeType.Prize.ID);
	 * tradeInfo.setRelativeid(order.getOrderid());
	 * tradeInfo.setTradeamount(order.getActualamount());
	 * tradeInfo.setAccountamount(accountAmount);
	 * tradeInfo.setAccountid(order.getAccountid()); tradeInfo.setInputtime(new
	 * Date()); tradeInfoMapper.insertSelective(tradeInfo);
	 * 
	 * account.setMoney(accountAmount);
	 * accountDetailMapper.updateByPrimaryKeySelective(account);
	 * order.setAccountamount(accountAmount);
	 * 
	 * List<AccountDetail> parentAccounts =
	 * customLotteryMapper.selectAccountBySupUserName(account.getSupusername());
	 * // Double bonusAmount = 0.0; for (AccountDetail parent : parentAccounts)
	 * { Double ratio = parent.getRatio();// 提成比例 Double payAmount = ratio *
	 * order.getActualamount();// 提成 accountAmount = account.getMoney().add(new
	 * BigDecimal(payAmount)); tradeInfo = new TradeInfo();
	 * tradeInfo.setTradetype(EnumType.TradeType.Order.ID);
	 * tradeInfo.setAccountid(parent.getAccountid());
	 * tradeInfo.setRelativetype(EnumType.RalativeType.Bonus.ID);
	 * tradeInfo.setTradeamount(payAmount);
	 * tradeInfo.setAccountamount(accountAmount);
	 * tradeInfoMapper.insertSelective(tradeInfo);
	 * parent.setMoney(accountAmount);
	 * accountDetailMapper.updateByPrimaryKeySelective(parent); // bonusAmount =
	 * bonusAmount+payAmount; }
	 * 
	 * lotteryOrderMapper.updateByPrimaryKeySelective(order); return order; }
	 * 
	 * public LotteryOrder updateLotteryOrder(LotteryOrder order) { return
	 * addTradeInfoByOrder(order); }
	 */

	// 添加帐户
	public boolean checkLotteryOrder(LotteryOrder order) {
		List<Map<String, String>> detailList = customLotteryMapper.selectOrderForCheck(order.getRoundid());
		Map<String, Double> tempMap = new HashMap<String, Double>();
		AccountDetail account = accountDetailMapper.selectByPrimaryKey(order.getAccountid());
		Double topAmount = account.getLimited();
		for (Map<String, String> detailMap : detailList) {
			tempMap.put(detailMap.get("ItemNo"), new Double(detailMap.get("Amount")));
		}
		for (LotteryOrderDetail detail : order.getOrderDetailList()) {
			String itemNo = detail.getItemno();
			Double amount = detail.getDetailamount();
			if (tempMap.containsKey(itemNo)) {
				tempMap.put(itemNo, tempMap.get(itemNo) + amount);
			} else {
				tempMap.put(itemNo, amount);
			}
		}
		if (Math.abs(getMapVal(tempMap, "BIG") - getMapVal(tempMap, "SMALL")) > topAmount) {
			return false;
		} else if (Math.abs(getMapVal(tempMap, "SINGLE") - getMapVal(tempMap, "DOUBLE")) > topAmount) {
			return false;
		} else {
			Double maxAmount = getMapVal(tempMap, "RED");
			Double minAmount = getMapVal(tempMap, "RED");
			if (maxAmount < getMapVal(tempMap, "BLUE")) {
				maxAmount = getMapVal(tempMap, "BLUE");
			}
			if (maxAmount < getMapVal(tempMap, "GREEN")) {
				maxAmount = getMapVal(tempMap, "GREEN");
			}

			if (minAmount > getMapVal(tempMap, "BLUE")) {
				minAmount = getMapVal(tempMap, "BLUE");
			}
			if (minAmount > getMapVal(tempMap, "GREEN")) {
				minAmount = getMapVal(tempMap, "GREEN");
			}
			if (maxAmount - minAmount > topAmount)
				return false;
		}
		return true;
	}

	private Double getMapVal(Map<String, Double> tempMap, String key) {
		if (tempMap.get(key) == null) {
			return 0.0;
		} else {
			return tempMap.get(key);
		}
	}

}
