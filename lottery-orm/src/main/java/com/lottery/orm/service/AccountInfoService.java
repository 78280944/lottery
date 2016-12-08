package com.lottery.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lottery.orm.bo.AccountInfo;
import com.lottery.orm.dao.AccountInfoMapper;

@Service
@Transactional
public class AccountInfoService {

	@Autowired
	private AccountInfoMapper accountInfoMapper;

	// 添加帐户
	public int addAccountInfo(AccountInfo account) {
		try {
			return accountInfoMapper.insertSelective(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	// 获取帐户
	public AccountInfo getAccountInfo(Integer accountId) {
		try {
			return accountInfoMapper.selectByPrimaryKey(accountId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 获取帐户
    public AccountInfo getAccountInfoByLogin(AccountInfo account) {
        try {
            return accountInfoMapper.selectByLogin(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	// 更新帐户
	public int updateAccountInfo(AccountInfo account) {
		try {
			return accountInfoMapper.updateByPrimaryKey(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	// 获取所有帐户
    public List<AccountInfo> getAllAccountInfo(AccountInfo account) {
        try {
            return accountInfoMapper.selectByExample(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //用户是否存在
	public AccountInfo selectByUser(AccountInfo paraInfo) {
		try {
			return accountInfoMapper.selectByUser(paraInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    //用户除了自己以外是否存在
	public AccountInfo selectByUserAndId(AccountInfo paraInfo) {
		try {
			return accountInfoMapper.selectByUserAndId(paraInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
