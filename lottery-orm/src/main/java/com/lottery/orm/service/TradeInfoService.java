package com.lottery.orm.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.bo.AccountInfo;
import com.lottery.orm.bo.OffAccountInfo;
import com.lottery.orm.bo.TradeInfo;
import com.lottery.orm.dao.AccountDetailMapper;
import com.lottery.orm.dao.AccountInfoMapper;
import com.lottery.orm.dao.OffAccountInfoMapper;
import com.lottery.orm.dao.TradeInfoMapper;
import com.lottery.orm.util.EnumType;

@Service
@Transactional
public class TradeInfoService {

    @Autowired
    private TradeInfoMapper tradeInfoMapper;

    @Autowired
    private AccountDetailMapper accountDetailMapper;
    
    // 添加出入金款项并更新账户
    public boolean addInoutTradeInfo(TradeInfo tradeInfo) {
    AccountDetail supAccountDetail = accountDetailMapper.selectByPrimaryKey(tradeInfo.getRelativeid());
	AccountDetail accountDetail = accountDetailMapper.selectByPrimaryKey(tradeInfo.getAccountid());
	Double supAccountAmount = supAccountDetail.getMoney().doubleValue();
	Double accountAmount = accountDetail.getMoney().doubleValue();
	
	
	if(tradeInfo.getRelativetype().equals(EnumType.RalativeType.In.ID)){
		supAccountAmount = supAccountAmount - tradeInfo.getTradeamount();
	    accountAmount = accountAmount + tradeInfo.getTradeamount();
	    tradeInfo.setTradeamount(tradeInfo.getTradeamount());
	}else if(tradeInfo.getRelativetype().equals(EnumType.RalativeType.Out.ID)){
	    accountAmount = accountAmount - tradeInfo.getTradeamount();
	    tradeInfo.setTradeamount(0.0 - tradeInfo.getTradeamount());//转换为负数
	}else{
	    return false;
	}
	tradeInfo.setTradetype(EnumType.TradeType.Inout.ID);
	tradeInfo.setAccountamount(new BigDecimal(accountAmount));
	tradeInfo.setInputtime(new Date());
	if (tradeInfoMapper.insertSelective(tradeInfo) > 0) {
		accountDetail.setMoney(new BigDecimal(accountAmount));
	    accountDetailMapper.updateByPrimaryKeySelective(accountDetail);
	    supAccountDetail.setMoney(new BigDecimal(supAccountAmount));
	    accountDetailMapper.updateByPrimaryKeySelective(supAccountDetail);
	    return true;
	}
	
	/*if(accountDetail.getOfftype().equals(EnumType.OffType.Play.ID)){
		
	}else if(accountDetail.getOfftype().equals(EnumType.OffType.Agency.ID)){
		
	}else{
		return false;
	}*/
	
	return false;
    }

}
