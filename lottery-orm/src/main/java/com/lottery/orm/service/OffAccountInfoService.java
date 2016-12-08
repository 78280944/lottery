package com.lottery.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.lottery.orm.bo.OffAccountInfo;
import com.lottery.orm.dao.OffAccountInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OffAccountInfoService {
	
	@Autowired
	private OffAccountInfoMapper offAccountInfoMapper;

	// 添加帐户
	public int addOffAccountInfo(OffAccountInfo account) {
		try {
			return offAccountInfoMapper.insertSelective(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	// 获取帐户
	public OffAccountInfo getOffAccountInfo(Integer accountId) {
		try {
			return offAccountInfoMapper.selectByPrimaryKey(accountId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 获取帐户
    public OffAccountInfo getOffAccountInfoByLogin(OffAccountInfo account) {
        try {
            return offAccountInfoMapper.selectByLogin(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
	// 获取帐户
    public OffAccountInfo getOffAccountInfoByUser(OffAccountInfo account) {
        try {
            return offAccountInfoMapper.selectByUser(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
	// 获取帐户
    public OffAccountInfo getOffAccountInfoByUserAndId(OffAccountInfo account) {
        try {
            return offAccountInfoMapper.selectByUserAndId(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
	// 更新帐户
	public int updateOffAccountInfo(OffAccountInfo account) {
		try {
			return offAccountInfoMapper.updateByPrimaryKey(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	// 获取所有帐户
    public List<OffAccountInfo> getAllOffAccountInfo(OffAccountInfo account) {
        try {
            return offAccountInfoMapper.selectByExample(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
}
