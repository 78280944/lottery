package com.lottery.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.dao.AccountDetailMapper;

@Service
@Transactional
public class AccountDetailService {
	
	@Autowired
	private AccountDetailMapper accountDetailMapper;

	// 添加帐户
	public int addAccountDetail(AccountDetail account) {
		try {
			return accountDetailMapper.insertSelective(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	// 获取帐户
	public AccountDetail getAccountDetail(Integer accountId) {
		try {
			return accountDetailMapper.selectByPrimaryKey(accountId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	// 更新帐户
	public int updateAccountDetail(AccountDetail account) {
		try {
			return accountDetailMapper.updateByPrimaryKey(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}
