package com.lottery.orm.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.bo.TradeInfo;
import com.lottery.orm.dao.AccountDetailMapper;
import com.lottery.orm.dao.TradeInfoMapper;
import com.lottery.orm.util.EnumType;

@Service
@Transactional
public class TradeInfoService {

    @Autowired
    private TradeInfoMapper tradeInfoMapper;

    @Autowired
    private AccountDetailMapper accountDetailMapper;

    // 添加出入金款项并更新帐户
    public boolean addInoutTradeInfo(TradeInfo tradeInfo) {
	AccountDetail accountDetail = accountDetailMapper.selectByPrimaryKey(tradeInfo.getAccountid());
	Double accountAmount = accountDetail.getMoney().doubleValue();
	if(tradeInfo.getRelativetype().equals(EnumType.RalativeType.In.ID)){
	    accountAmount = accountAmount + tradeInfo.getTradeamount();
	}else if(tradeInfo.getRelativetype().equals(EnumType.RalativeType.Out.ID)){
	    accountAmount = accountAmount - tradeInfo.getTradeamount();
	    tradeInfo.setTradeamount(0.0 - tradeInfo.getTradeamount());//转换为负数
	}else{
	    return false;
	}
	tradeInfo.setTradetype(EnumType.TradeType.Inout.ID);
	tradeInfo.setRelativeid(tradeInfo.getAccountid());
	tradeInfo.setAccountamount(new BigDecimal(accountAmount));
	tradeInfo.setInputtime(new Date());
	if (tradeInfoMapper.insertSelective(tradeInfo) > 0) {
	    AccountDetail updateAccount = new AccountDetail();
	    updateAccount.setAccountid(tradeInfo.getAccountid());
	    updateAccount.setMoney(new BigDecimal(accountAmount));
	    accountDetailMapper.updateByPrimaryKeySelective(updateAccount);
	    return true;
	}
	return false;
    }

}
