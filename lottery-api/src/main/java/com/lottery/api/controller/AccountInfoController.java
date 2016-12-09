package com.lottery.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lottery.api.dto.AccountInfoVo;
import com.lottery.api.dto.LoginParamVo;
import com.lottery.api.util.ToolsUtil;
import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.bo.AccountInfo;
import com.lottery.orm.bo.OffAccountInfo;
import com.lottery.orm.dao.AccountDetailMapper;
import com.lottery.orm.dao.AccountInfoMapper;
import com.lottery.orm.dao.OffAccountInfoMapper;
import com.lottery.orm.dto.AccountInfoDto;
import com.lottery.orm.result.AccountListResult;
import com.lottery.orm.result.AccountResult;
import com.lottery.orm.service.AccountInfoService;
import com.lottery.orm.util.EnumType;
import com.lottery.orm.util.MessageTool;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RequestMapping(value = "/account", produces = {"application/json;charset=UTF-8"})
@Api(value = "/account", description = "玩家帐号信息接口")
@Controller
public class AccountInfoController {
	public static final Logger LOG = Logger.getLogger(AccountInfoController.class);
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
    private AccountInfoService accountInfoService;
	
	@Autowired
    private AccountInfoMapper accountInfoMapper;
	
	@Autowired
    private AccountDetailMapper accountDetailMapper;
	
	@Autowired
    private OffAccountInfoMapper offAccountInfoMapper;
	
	@ApiOperation(value = "获取玩家信息", notes = "获取玩家信息", httpMethod = "POST")
	@RequestMapping(value = "/getAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public AccountResult getAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody LoginParamVo param) throws Exception {
		AccountResult result = new AccountResult();
		try {
			
			String username = param.getUsername();
			String password = param.getPassword();
			
			//参数合规性校验，必要参数不能为空
			if (ToolsUtil.isEmptyTrim(username)||ToolsUtil.isEmptyTrim(password)){
			      result.fail(MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			param.setPassword(DigestUtils.md5Hex(password));
		    AccountInfo paraInfo = mapper.map(param, AccountInfo.class);
		    AccountInfo accountInfo = accountInfoMapper.selectByLogin(paraInfo);
		    if(accountInfo!=null){
		    	AccountDetail accountDetail = accountDetailMapper.selectByUserId(accountInfo.getUserid(), "3");
				AccountInfoDto rAcDto = new AccountInfoDto();
				rAcDto.setUserid(null==accountInfo.getUserid()||"".equals(accountInfo.getUserid())||0.0==accountInfo.getUserid() ?0:accountInfo.getUserid());
				rAcDto.setUsername(accountInfo.getUsername());
				rAcDto.setAusername(accountInfo.getAusername());
				rAcDto.setPassword(accountInfo.getPassword());
				rAcDto.setLimited(null==accountInfo.getLimited()||"".equals(accountInfo.getLimited())||0.0==accountInfo.getLimited() ?0.0:accountInfo.getLimited());
				rAcDto.setRatio(null==accountInfo.getRatio()||"".equals(accountInfo.getRatio())||0.0==accountInfo.getRatio() ?0:accountInfo.getRatio());
				rAcDto.setState(null==accountInfo.getState()||"".equals(accountInfo.getState()) ? "":accountInfo.getState());
				rAcDto.setSupusername(null==accountInfo.getSupusername()||"".equals(accountInfo.getSupusername()) ? "":accountInfo.getSupusername());
				rAcDto.setLevel(null==accountInfo.getLevel()||"".equals(accountInfo.getLevel()) ? "":accountInfo.getLevel());
				rAcDto.setAccountAmount(accountDetail.getMoney());
				result.success(rAcDto);
		    }else{
		    	result.fail(MessageTool.Code_3001);
		    }
			LOG.info(username+","+result.getMessage()+","+new Date());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;

	}
	
	@ApiOperation(value = "新增玩家", notes = "新增玩家", httpMethod = "POST")
	@RequestMapping(value = "/addAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public AccountResult addAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody AccountInfo param) throws Exception {
		AccountResult result = new AccountResult();
		try {
			String username = param.getUsername();
			String password = param.getPassword();
			String supusername = param.getSupusername();
			String level = param.getLevel();
			Double limited =  null;
			Double ratio = null;
			
			if (null != param.getLimited())
				limited = param.getLimited();
			if (null != param.getRatio())
				ratio = param.getRatio();
			
			
			//参数合规性校验，必要参数不能为空
			if (ToolsUtil.isEmptyTrim(username)||ToolsUtil.isEmptyTrim(password)){
			      result.fail("用户名，密码",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			

			//最长14个英文或者数字组合
			if (ToolsUtil.validatName(username)){
			      result.fail("用户名",MessageTool.Code_1006);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			if (ToolsUtil.isEmptyTrim(supusername)||ToolsUtil.isEmptyTrim(level)){
			      result.fail("管理操作员，代理级别",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			/*
			//6-14位数字、字母、符号组合
			if (ToolsUtil.validateSignName(password)){
			      result.fail("密码",MessageTool.Code_1007);
			      LOG.info(result.getMessage());
			      return result;
			}
			*/
			
			//数字型
			if (null != limited){
				if (ToolsUtil.isNumeric(String.valueOf(limited))){
				      result.fail("点数限额",MessageTool.Code_1004);
				      LOG.info(result.getMessage());
				      return result;		
				}
			}
			
			//数字型
			if (null != ratio){
				if (ToolsUtil.isNumeric(String.valueOf(ratio))){
				      result.fail("洗码比",MessageTool.Code_1004);
				      LOG.info(result.getMessage());
				      return result;		
				}
			}
			
			//玩家是否存在，用户名不能一致
			param.setPassword(DigestUtils.md5Hex(password));
		    AccountInfo paraInfo = mapper.map(param, AccountInfo.class);
		    AccountInfo accountInfo = accountInfoMapper.selectByUsername(paraInfo.getUsername());
		    if (accountInfo!=null){
			      result.fail(username,MessageTool.Code_2005);
		    }else{
			    paraInfo.setState("1");//默认状态正常
			    paraInfo.setInputdate(new Date());
			    accountInfoService.addAccountInfo(paraInfo);
			    result.success();
		    }
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	@ApiOperation(value = "修改玩家", notes = "修改玩家", httpMethod = "POST")
	@RequestMapping(value = "/updateAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public AccountResult updateAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody AccountInfo param) throws Exception {
		AccountResult result = new AccountResult();
		try {
			int serialno = param.getUserid();
			String username = param.getUsername();
			String ausername = param.getAusername();
			String password = param.getPassword();
			String supusername = param.getSupusername();
			String level = param.getLevel();
			String state = param.getState();
			Double limited =  null;
			Double ratio = null;
			
			if (null != param.getLimited())
				limited = param.getLimited();
			if (null != param.getRatio())
				ratio = param.getRatio();
			
			
			//参数合规性校验，必要参数不能为空
			if (ToolsUtil.isEmptyTrim(username)){
			      result.fail("用户名",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			if (ToolsUtil.isEmptyTrim(supusername)||ToolsUtil.isEmptyTrim(level)){
			      result.fail("管理操作员，代理级别",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			System.out.println("9---"+serialno);
			if (0==serialno){
			      result.fail("会员流水号",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			/*
			//最长14个英文或者数字组合
			if (ToolsUtil.validatName(username)){
			      result.fail("用户名",MessageTool.Code_1006);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			
			//6-14位数字、字母、符号组合
			if (ToolsUtil.validateSignName(password)){
			      result.fail("密码",MessageTool.Code_1007);
			      LOG.info(result.getMessage());
			      return result;
			}
			*/
			
			/*
			//数字型
			if (null != limited){
				if (ToolsUtil.isNumeric(String.valueOf(limited))){
				      result.fail("点数限额",MessageTool.Code_1004);
				      LOG.info(result.getMessage());
				      return result;		
				}
			}
			
			//数字型
			if (null != ratio){
				if (ToolsUtil.isNumeric(String.valueOf(ratio))){
				      result.fail("洗码比",MessageTool.Code_1004);
				      LOG.info(result.getMessage());
				      return result;		
				}
			}
			
			
			//状态判断
			if (ToolsUtil.betweenRange(state)){
			      result.fail("状态",MessageTool.Code_1005);
			      LOG.info(result.getMessage());
			      return result;	
			}
			*/
			AccountInfo accountInfo = accountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(accountInfo==null){
			      result.fail(MessageTool.Code_3001);
			}else{
				password = DigestUtils.md5Hex(password);	
			    AccountInfo paraInfo = mapper.map(param, AccountInfo.class);
			    System.out.println("9-----------"+password+"..."+limited+".."+paraInfo.getLimited());
			    AccountInfo accountInfocheck = accountInfoMapper.selectByUserAndId(paraInfo);
			    if (accountInfocheck!=null){
				      result.fail(username,MessageTool.Code_2005);
				      LOG.info(result.getMessage());
				      return result;	
			    }
			    paraInfo.setUsername(null==param.getUsername()||"".equals(param.getUsername()) ? accountInfo.getUsername():param.getUsername());
			    paraInfo.setAusername(null==param.getAusername()||"".equals(param.getAusername()) ? accountInfo.getAusername():param.getAusername());
			    paraInfo.setPassword(null==param.getPassword()||"".equals(param.getPassword()) ? accountInfo.getPassword():password);
			    paraInfo.setLimited(null==param.getLimited()||"".equals(param.getLimited())||0.0==param.getLimited() ? accountInfo.getLimited():param.getLimited());
			    paraInfo.setRatio(null==param.getRatio()||"".equals(param.getRatio())||0.0==param.getRatio() ? accountInfo.getRatio():param.getRatio());
			    paraInfo.setState(null==param.getState()||"".equals(param.getState()) ?  accountInfo.getState():param.getState());
			    paraInfo.setUpdateip(null==param.getUpdateip()||"".equals(param.getUpdateip()) ? accountInfo.getUpdateip():param.getUpdateip());
			    paraInfo.setUpdatedate(new Date());
			    accountInfoService.updateAccountInfo(paraInfo);
			    result.success();
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}

	@ApiOperation(value = "获取玩家列表", notes = "获取该代理下的玩家列表", httpMethod = "POST")
	@RequestMapping(value = "/getAllAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public AccountListResult getAllAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody AccountInfoVo param) throws Exception {
	    AccountListResult result = new AccountListResult();
		try {
			OffAccountInfo offacount = offAccountInfoMapper.selectByUseridAndType(param.getUserid(), EnumType.OffType.Agency.ID);
			
			if (offacount==null){
				result.fail(MessageTool.Code_3001);
			}else{
				List<AccountInfo> accountInfos = accountInfoMapper.selectBySupusername(offacount.getUsername(), param.getBeginRow(), param.getPageSize());
	
				List<AccountInfoDto> list = new ArrayList<AccountInfoDto>();
				for (int i = 0;i<accountInfos.size();i++){
					AccountInfoDto rAcDto = new AccountInfoDto();
			        rAcDto.setUserid(null==accountInfos.get(i).getUserid()||"".equals(accountInfos.get(i).getUserid())||0==accountInfos.get(i).getUserid() ?0:accountInfos.get(i).getUserid());
			        rAcDto.setUsername(null==accountInfos.get(i).getUsername()||"".equals(accountInfos.get(i).getUsername()) ?"":accountInfos.get(i).getUsername());
			        rAcDto.setAusername(null==accountInfos.get(i).getAusername()||"".equals(accountInfos.get(i).getAusername()) ?"":accountInfos.get(i).getAusername());
			        rAcDto.setPassword(null==accountInfos.get(i).getPassword()||"".equals(accountInfos.get(i).getPassword()) ?"":accountInfos.get(i).getPassword());
			        rAcDto.setLimited(null==accountInfos.get(i).getLimited()||"".equals(accountInfos.get(i).getLimited())||0.0==accountInfos.get(i).getLimited() ?0.0:accountInfos.get(i).getLimited());
			        rAcDto.setRatio(null==accountInfos.get(i).getRatio()||"".equals(accountInfos.get(i).getRatio())||0.0==accountInfos.get(i).getRatio() ?0.0:accountInfos.get(i).getRatio());
			        rAcDto.setLevel(null==accountInfos.get(i).getLevel()||"".equals(accountInfos.get(i).getLevel()) ?"":accountInfos.get(i).getLevel());
			        rAcDto.setState(null==accountInfos.get(i).getState()||"".equals(accountInfos.get(i).getState()) ?"":accountInfos.get(i).getState());
			        rAcDto.setSupusername(null==accountInfos.get(i).getSupusername()||"".equals(accountInfos.get(i).getSupusername()) ?"":accountInfos.get(i).getSupusername());
					list.add(rAcDto);
				}
				result.success(list);
			}
		    
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
}
