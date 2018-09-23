package com.lottery.api.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lottery.api.dto.LoginParamVo;
import com.lottery.api.util.ToolsUtil;
import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.bo.OffAccountInfo;
import com.lottery.orm.dto.SubAccountDto;
import com.lottery.orm.result.SubAccountListResult;
import com.lottery.orm.result.SubAccountResult;
import com.lottery.orm.service.AccountDetailService;
import com.lottery.orm.service.OffAccountInfoService;
import com.lottery.orm.util.MessageTool;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RequestMapping(value = "/subAccount", produces = {"application/json;charset=UTF-8"})
@Api(value = "/subAccount", description = "帐户信息接口")
@Controller
public class SubAccountInfoController {
	public static final Logger LOG = Logger.getLogger(SubAccountInfoController.class);
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
    private OffAccountInfoService OffAccountInfoService;
	@Autowired
	private AccountDetailService accountDetailService;
	
	@ApiOperation(value = "获取结果", notes = "获取结果", httpMethod = "POST")
	@RequestMapping(value = "/getSubAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public SubAccountResult getSubAccountInfo(@ApiParam(value = "Json参数", required = true) @RequestBody LoginParamVo param) throws Exception {
		SubAccountResult result = new SubAccountResult();
		try {
			
			String username = param.getUsername();
			String password = param.getPassword();

			//参数合规性校验，必要参数不能为空
			if (ToolsUtil.isEmptyTrim(username)||ToolsUtil.isEmptyTrim(password)){
			      result.fail(MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			password = DigestUtils.md5Hex(password);
			OffAccountInfo paraInfo = mapper.map(param, OffAccountInfo.class);
		    //SubAccountInfo SubAccountInfo = SubAccountInfoService.getSubAccountInfoByLogin(paraInfo);
		    OffAccountInfo SubAccountInfo = OffAccountInfoService.getOffAccountInfoByLogin(paraInfo);
			   
		    if(SubAccountInfo!=null){
		      SubAccountDto rAcDto = new SubAccountDto();
		      rAcDto.setAccountid(null==SubAccountInfo.getAccountid()||"".equals(SubAccountInfo.getAccountid())||0==SubAccountInfo.getAccountid() ?0:SubAccountInfo.getAccountid());
		      rAcDto.setUsername(null==SubAccountInfo.getUsername()||"".equals(SubAccountInfo.getUsername()) ?"":SubAccountInfo.getUsername());
		      rAcDto.setAusername(null==SubAccountInfo.getAusername()||"".equals(SubAccountInfo.getAusername()) ?"":SubAccountInfo.getAusername());
		      rAcDto.setPassword(null==SubAccountInfo.getPassword()||"".equals(SubAccountInfo.getPassword()) ?"":SubAccountInfo.getPassword());
		      rAcDto.setQuery(null==SubAccountInfo.getQuery()||"".equals(SubAccountInfo.getQuery()) ?"":SubAccountInfo.getQuery());
		      rAcDto.setManage(null==SubAccountInfo.getManage()||"".equals(SubAccountInfo.getManage()) ?"":SubAccountInfo.getManage());
		      rAcDto.setState(null==SubAccountInfo.getState()||"".equals(SubAccountInfo.getState()) ?"":SubAccountInfo.getState());
		      rAcDto.setSupusername(null==SubAccountInfo.getSupusername()||"".equals(SubAccountInfo.getSupusername()) ?"":SubAccountInfo.getSupusername());
		      rAcDto.setLevel(null==SubAccountInfo.getLevel()||"".equals(SubAccountInfo.getLevel()) ?"":SubAccountInfo.getLevel());
		      rAcDto.setOfftype(null==SubAccountInfo.getOfftype()||"".equals(SubAccountInfo.getOfftype()) ?"":SubAccountInfo.getOfftype());		      
		      result.success(rAcDto);        
		    }else{
		      result.fail(MessageTool.FailCode);
		    }
			LOG.info(result.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		return result;

	}
	
	@ApiOperation(value = "新增用户", notes = "新增用户", httpMethod = "POST")
	@RequestMapping(value = "/addSubAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public SubAccountResult addSubAccountInfo(@ApiParam(value = "Json参数", required = true) @RequestBody OffAccountInfo param) throws Exception {
		SubAccountResult result = new SubAccountResult();
		try {
			
			String username = param.getUsername();
			String password = param.getPassword();
			String supusername = param.getSupusername();	
			String level = param.getLevel();
			String query = param.getQuery();
			String manage = param.getManage();
			//参数合规性校验，必要参数不能为空
			if (ToolsUtil.isEmptyTrim(username)||ToolsUtil.isEmptyTrim(password)){
			      result.fail("用户名，密码",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			//参数合规性校验，必要参数不能为空
			if (ToolsUtil.isEmptyTrim(supusername)||ToolsUtil.isEmptyTrim(level)){
			      result.fail("管理操作员,代理级别",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			if (null!=query||!"".equals(query)){
		        if (ToolsUtil.checkQuery(query)){
			      result.fail("查询权限设置",MessageTool.Code_1005);
			      LOG.info(result.getMessage());
			      return result;
		        }
			}
			
			if (null!=manage||!"".equals(manage)){
				 if (ToolsUtil.checkManage(manage)){
			      result.fail("管理权限设置",MessageTool.Code_1005);
			      LOG.info(result.getMessage());
			      return result;
				 }
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

			param.setPassword(DigestUtils.md5Hex(password));
		    OffAccountInfo paraInfo = mapper.map(param, OffAccountInfo.class);
		    paraInfo.setState("1");//默认状态正常
		    paraInfo.setOfftype("2");
		    paraInfo.setInputdate(new Date());
		    System.out.println("9-------------"+paraInfo.getLevel());
		    /*
		    //paraInfo.setSerialno(ToolsUtil.getCurrentSerial());
		    paraInfo.setUsername(param.getUsername());
		    paraInfo.setAusername(param.getAusername());
		    paraInfo.setPassword(param.getPassword());
		    paraInfo.setLimited(param.getLimited());
		    paraInfo.setRatio(param.getRatio());
		    paraInfo.setState("1");//默认状态正常
		    paraInfo.setIp(param.getIp());
		    paraInfo.setInputdate(new Date());*/
		    OffAccountInfoService.addOffAccountInfo(paraInfo);
		    
		    AccountDetail accountDetail = new AccountDetail();
		    accountDetail.setUserid(paraInfo.getAccountid());
		    accountDetail.setUsername(paraInfo.getUsername());
		    accountDetail.setLimited(paraInfo.getLimited());
		    accountDetail.setRatio(paraInfo.getRatio());
		    accountDetail.setPercentage(0.0);
		    accountDetail.setState("1");
		    accountDetail.setSupusername(paraInfo.getSupusername());
		    accountDetail.setLevel(paraInfo.getLevel());
		    accountDetail.setOfftype("2");
		    accountDetail.setMoney(BigDecimal.valueOf(0.0));
		    accountDetailService.addAccountDetail(accountDetail);
		    
		    result.success();
			LOG.info(result.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	@ApiOperation(value = "修改用户", notes = "修改用户", httpMethod = "POST")
	@RequestMapping(value = "/updateSubAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public SubAccountResult updateSubAccountInfo(@ApiParam(value = "Json参数", required = true) @RequestBody OffAccountInfo param) throws Exception {
		SubAccountResult result = new SubAccountResult();
		try {
			String username = param.getUsername();
			String password = param.getPassword();
			String supusername = param.getSupusername();	
			String level = param.getLevel();
			String query = param.getQuery();
			String manage = param.getManage();
			
			//参数合规性校验，必要参数不能为空
			if (ToolsUtil.isEmptyTrim(username)){
			      result.fail("用户名",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			//操作管理，必要参数不能为空		
			if (ToolsUtil.isEmptyTrim(supusername)||ToolsUtil.isEmptyTrim(level)){
			      result.fail("管理操作员,代理级别",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			if (null!=query||!"".equals(query)){
		        if (ToolsUtil.checkQuery(query)){
			      result.fail("查询权限设置",MessageTool.Code_1005);
			      LOG.info(result.getMessage());
			      return result;
		        }
			}
			
			if (null!=manage||!"".equals(manage)){
				 if (ToolsUtil.checkManage(manage)){
			      result.fail("管理权限设置",MessageTool.Code_1005);
			      LOG.info(result.getMessage());
			      return result;
				 }
			}
			
			OffAccountInfo SubAccountInfo = OffAccountInfoService.getOffAccountInfo(param.getAccountid());
			if(SubAccountInfo==null){
			      result.fail(MessageTool.FailCode);
			      LOG.info(result.getMessage());
			      return result;
			}
			password = DigestUtils.md5Hex(password);	
		    OffAccountInfo paraInfo = mapper.map(param, OffAccountInfo.class);
		    paraInfo.setUsername(null==param.getUsername()||"".equals(param.getUsername()) ? SubAccountInfo.getUsername():param.getUsername());
		    paraInfo.setAusername(null==param.getAusername()||"".equals(param.getAusername()) ? SubAccountInfo.getAusername():param.getAusername());
		    System.out.println("800--"+param.getPassword());
		    System.out.println("800--"+SubAccountInfo.getPassword());
		    paraInfo.setPassword(null==param.getPassword()||"".equals(param.getPassword()) ? SubAccountInfo.getPassword():password);
		    paraInfo.setQuery(null==param.getQuery()||"".equals(param.getQuery()) ? SubAccountInfo.getQuery():param.getQuery());
		    paraInfo.setManage(null==param.getManage()||"".equals(param.getManage()) ? SubAccountInfo.getManage():param.getManage());
		    paraInfo.setLevel(null==param.getLevel()||"".equals(param.getLevel()) ? SubAccountInfo.getLevel():param.getLevel());
		    paraInfo.setOfftype(SubAccountInfo.getOfftype());   
		    paraInfo.setState(null==param.getState()||"".equals(param.getState()) ?  SubAccountInfo.getState():param.getState());
		    paraInfo.setUpdateip(null==param.getUpdateip()||"".equals(param.getUpdateip()) ? SubAccountInfo.getUpdateip():param.getUpdateip());
		    System.out.println("9-------------d-"+ToolsUtil.getCurrentTime());
		    paraInfo.setUpdatedate(new Date());
		    OffAccountInfoService.updateOffAccountInfo(paraInfo);
		    
		    AccountDetail accountDetail = new AccountDetail();
		    accountDetail.setUserid(paraInfo.getAccountid());
		    accountDetail.setUsername(paraInfo.getUsername());
		    accountDetail.setLimited(paraInfo.getLimited());
		    accountDetail.setRatio(paraInfo.getRatio());
		    accountDetail.setPercentage(paraInfo.getPercentage());
		    accountDetail.setState(paraInfo.getState());
		    accountDetail.setSupusername(paraInfo.getSupusername());
		    accountDetail.setLevel(paraInfo.getLevel());
		    accountDetail.setOfftype(paraInfo.getOfftype());
		    
		    accountDetailService.updateAccountDetail(accountDetail);
		    
		    result.success();
			LOG.info(result.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		return result;
	}

	@ApiOperation(value = "获取用户列表", notes = "获取用户列表", httpMethod = "POST")
	@RequestMapping(value = "/getAllSubAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public SubAccountListResult getAllSubAccountInfo(@ApiParam(value = "Json参数", required = true) @RequestBody OffAccountInfo param) throws Exception {
	    	SubAccountListResult result = new SubAccountListResult();
		try {
			
			String supusername = param.getSupusername();	
			String level = param.getLevel();
			
			//操作管理，必要参数不能为空		
			if (ToolsUtil.isEmptyTrim(supusername)||ToolsUtil.isEmptyTrim(level)){
			      result.fail("管理操作员,代理级别",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			List<OffAccountInfo> SubAccountInfos = OffAccountInfoService.getAllOffAccountInfo(param);
			if(SubAccountInfos==null){
			      result.fail(MessageTool.FailCode);
			      LOG.info(result.getMessage());
			      return result;
			}
			List<SubAccountDto> list = new ArrayList<SubAccountDto>();
			for (int i = 0;i<SubAccountInfos.size();i++){
			      SubAccountDto rAcDto = new SubAccountDto();
			      rAcDto.setAccountid(null==SubAccountInfos.get(i).getAccountid()||"".equals(SubAccountInfos.get(i).getAccountid())||0==SubAccountInfos.get(i).getAccountid() ?0:SubAccountInfos.get(i).getAccountid());
			      rAcDto.setUsername(null==SubAccountInfos.get(i).getUsername()||"".equals(SubAccountInfos.get(i).getUsername()) ?"":SubAccountInfos.get(i).getUsername());
			      rAcDto.setAusername(null==SubAccountInfos.get(i).getAusername()||"".equals(SubAccountInfos.get(i).getAusername()) ?"":SubAccountInfos.get(i).getAusername());
			      rAcDto.setPassword(null==SubAccountInfos.get(i).getPassword()||"".equals(SubAccountInfos.get(i).getPassword()) ?"":SubAccountInfos.get(i).getPassword());
			      rAcDto.setQuery(null==SubAccountInfos.get(i).getQuery()||"".equals(SubAccountInfos.get(i).getQuery()) ?"":SubAccountInfos.get(i).getQuery());
			      rAcDto.setManage(null==SubAccountInfos.get(i).getManage()||"".equals(SubAccountInfos.get(i).getManage()) ?"":SubAccountInfos.get(i).getManage());
			      rAcDto.setState(null==SubAccountInfos.get(i).getState()||"".equals(SubAccountInfos.get(i).getState()) ?"":SubAccountInfos.get(i).getState());
			      rAcDto.setSupusername(null==SubAccountInfos.get(i).getSupusername()||"".equals(SubAccountInfos.get(i).getSupusername()) ?"":SubAccountInfos.get(i).getSupusername());
			      rAcDto.setLevel(null==SubAccountInfos.get(i).getLevel()||"".equals(SubAccountInfos.get(i).getLevel()) ?"":SubAccountInfos.get(i).getLevel());
			      rAcDto.setOfftype(null==SubAccountInfos.get(i).getOfftype()||"".equals(SubAccountInfos.get(i).getOfftype()) ?"":SubAccountInfos.get(i).getOfftype());	 
			      list.add(rAcDto);
			}
		    result.success(list);
			LOG.info(result.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
}
