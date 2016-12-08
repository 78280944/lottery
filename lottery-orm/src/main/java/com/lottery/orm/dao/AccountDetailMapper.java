package com.lottery.orm.dao;

import org.apache.ibatis.annotations.Param;

import com.lottery.orm.bo.AccountDetail;

public interface AccountDetailMapper {
    int deleteByPrimaryKey(Integer accountid);

    int insert(AccountDetail record);

    int insertSelective(AccountDetail record);

    AccountDetail selectByPrimaryKey(Integer accountid);

    int updateByPrimaryKeySelective(AccountDetail record);

    int updateByPrimaryKey(AccountDetail record);
    
    AccountDetail selectByUserId(@Param("userid")Integer userid, @Param("offtype")String offtype);
}