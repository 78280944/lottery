package com.lottery.orm.dao;

import java.util.List;

import com.lottery.orm.bo.AccountInfo;

public interface AccountInfoMapper {
    
    int deleteByPrimaryKey(Integer serialno);

    int insert(AccountInfo record);

    int insertSelective(AccountInfo record);

    AccountInfo selectByPrimaryKey(Integer serialno);

    int updateByPrimaryKeySelective(AccountInfo record);

    int updateByPrimaryKey(AccountInfo record);
  
    //get account info when login
    AccountInfo selectByLogin(AccountInfo record);
    
    List<AccountInfo> selectByExample(AccountInfo example);
    
    AccountInfo selectByUser(AccountInfo record);
    
    AccountInfo selectByUserAndId(AccountInfo record);
}