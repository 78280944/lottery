package com.lottery.orm.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.bo.AccountInfo;
import com.lottery.orm.dao.AccountDetailMapper;
import com.lottery.orm.dao.AccountInfoMapper;

@Service
@Transactional
public class AccountInfoService {

	@Autowired
	private AccountInfoMapper accountInfoMapper;

	@Autowired
	private AccountDetailMapper accountDetailMapper;

	// 添加账户
	public void addAccountInfo(AccountInfo paraInfo) {
		accountInfoMapper.insertSelective(paraInfo);

		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setUserid(paraInfo.getUserid());
		accountDetail.setUsername(paraInfo.getUsername());
		accountDetail.setLimited(paraInfo.getLimited());
		accountDetail.setRatio(paraInfo.getRatio());
		accountDetail.setPercentage(0.0);
		accountDetail.setState("1");
		accountDetail.setSupusername(paraInfo.getSupusername());
		accountDetail.setLevel(paraInfo.getLevel());
		accountDetail.setOfftype("3");
		accountDetail.setMoney(BigDecimal.valueOf(0.0));
		accountDetailMapper.insertSelective(accountDetail);
	}

	// 更新账户
	public void updateAccountInfo(AccountInfo paraInfo) {
		accountInfoMapper.updateByPrimaryKey(paraInfo);
		
		AccountDetail accountDetail = new AccountDetail();
	    accountDetail.setUserid(paraInfo.getUserid());
	    accountDetail.setUsername(paraInfo.getUsername());
	    accountDetail.setLimited(paraInfo.getLimited());
	    accountDetail.setRatio(paraInfo.getRatio());
	    accountDetail.setPercentage(0.0);
	    accountDetail.setState(paraInfo.getState());
	    accountDetail.setSupusername(paraInfo.getSupusername());
	    accountDetail.setLevel(paraInfo.getLevel());
	    accountDetail.setOfftype("3");
	    accountDetailMapper.updateByPrimaryKeySelective(accountDetail);
	}

}
