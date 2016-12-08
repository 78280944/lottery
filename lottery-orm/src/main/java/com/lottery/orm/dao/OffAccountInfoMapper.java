package com.lottery.orm.dao;

import java.util.List;

import com.lottery.orm.bo.OffAccountInfo;

public interface OffAccountInfoMapper {
    
	int deleteByPrimaryKey(Integer serialno);

    int insert(OffAccountInfo record);

    int insertSelective(OffAccountInfo record);

    OffAccountInfo selectByPrimaryKey(Integer serialno);

    int updateByPrimaryKeySelective(OffAccountInfo record);

    int updateByPrimaryKey(OffAccountInfo record);
    
    //get account info when login
    OffAccountInfo selectByLogin(OffAccountInfo record);
    
    OffAccountInfo selectByUser(OffAccountInfo record);
    
    OffAccountInfo selectByUserAndId(OffAccountInfo record);
    
    List<OffAccountInfo> selectByExample(OffAccountInfo example);
}