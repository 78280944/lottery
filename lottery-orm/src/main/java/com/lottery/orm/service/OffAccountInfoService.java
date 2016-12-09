package com.lottery.orm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.bo.OffAccountInfo;
import com.lottery.orm.dao.AccountDetailMapper;
import com.lottery.orm.dao.OffAccountInfoMapper;

@Service
@Transactional
public class OffAccountInfoService {

	@Autowired
	private OffAccountInfoMapper offAccountInfoMapper;

	@Autowired
	private AccountDetailMapper accountDetailMapper;

	// 添加帐户
	public void addOffAccountInfo(OffAccountInfo paraInfo) {
		offAccountInfoMapper.insertSelective(paraInfo);
		
		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setUserid(paraInfo.getUserid());
		accountDetail.setUsername(paraInfo.getUsername());
		accountDetail.setLimited(paraInfo.getLimited());
		accountDetail.setRatio(paraInfo.getRatio());
		accountDetail.setPercentage(paraInfo.getPercentage());
		accountDetail.setState(paraInfo.getState());
		accountDetail.setSupusername(paraInfo.getSupusername());
		accountDetail.setLevel(paraInfo.getLevel());
		accountDetail.setOfftype(paraInfo.getOfftype());
		accountDetailMapper.insertSelective(accountDetail);
	}

	// 更新帐户
	public void updateOffAccountInfo(OffAccountInfo paraInfo) {
		offAccountInfoMapper.updateByPrimaryKey(paraInfo);

		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setUserid(paraInfo.getUserid());
		accountDetail.setUsername(paraInfo.getUsername());
		accountDetail.setLimited(paraInfo.getLimited());
		accountDetail.setRatio(paraInfo.getRatio());
		accountDetail.setPercentage(paraInfo.getPercentage());
		accountDetail.setState(paraInfo.getState());
		accountDetail.setSupusername(paraInfo.getSupusername());
		accountDetail.setLevel(paraInfo.getLevel());
		accountDetail.setOfftype(paraInfo.getOfftype());
		accountDetailMapper.updateByPrimaryKeySelective(accountDetail);
	}

}
