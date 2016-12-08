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
import com.lottery.orm.dto.OffAccountDto;
import com.lottery.orm.result.OffAccountListResult;
import com.lottery.orm.result.OffAccountResult;
import com.lottery.orm.result.RestResult;
import com.lottery.orm.service.AccountDetailService;
import com.lottery.orm.service.OffAccountInfoService;
import com.lottery.orm.util.MessageTool;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RequestMapping(value = "/offAccount", produces = {"application/json;charset=UTF-8"})
@Api(value = "/offAccount", description = "帐户信息接口")
@Controller
public class OffAccountInfoController {
	public static final Logger LOG = Logger.getLogger(OffAccountInfoController.class);
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
    private OffAccountInfoService OffAccountInfoService;
	@Autowired
	private AccountDetailService accountDetailService;
	
	@ApiOperation(value = "获取结果", notes = "获取结果", httpMethod = "POST")
	@RequestMapping(value = "/getOffAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public OffAccountResult getOffAccountInfo(@ApiParam(value = "Json参数", required = true) @RequestBody LoginParamVo param) throws Exception {
		OffAccountResult result = new OffAccountResult();
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
		    OffAccountInfo paraInfo = mapper.map(param, OffAccountInfo.class);
		    OffAccountInfo OffAccountInfo = OffAccountInfoService.getOffAccountInfoByLogin(paraInfo);
		    if(OffAccountInfo!=null){
		      OffAccountDto rAcDto = new OffAccountDto();
		      rAcDto.setUserid(null==OffAccountInfo.getUserid()||"".equals(OffAccountInfo.getUserid())||0==OffAccountInfo.getUserid() ?0:OffAccountInfo.getUserid());
		      rAcDto.setUsername(null==OffAccountInfo.getUsername()||"".equals(OffAccountInfo.getUsername()) ?"":OffAccountInfo.getUsername());
		      rAcDto.setAusername(null==OffAccountInfo.getAusername()||"".equals(OffAccountInfo.getAusername()) ?"":OffAccountInfo.getAusername());
		      rAcDto.setPassword(null==OffAccountInfo.getPassword()||"".equals(OffAccountInfo.getPassword()) ?"":OffAccountInfo.getPassword());
		      rAcDto.setLimited(null==OffAccountInfo.getLimited()||"".equals(OffAccountInfo.getLimited())||0.0==OffAccountInfo.getLimited() ?0.0:OffAccountInfo.getLimited());
		      rAcDto.setRatio(null==OffAccountInfo.getRatio()||"".equals(OffAccountInfo.getRatio())||0.0==OffAccountInfo.getRatio() ?0.0:OffAccountInfo.getRatio());
		      rAcDto.setPercentage(null==OffAccountInfo.getPercentage()||"".equals(OffAccountInfo.getPercentage())||0.0==OffAccountInfo.getPercentage() ?0.0:OffAccountInfo.getPercentage());
		      rAcDto.setQuery(null==OffAccountInfo.getQuery()||"".equals(OffAccountInfo.getQuery()) ?"":OffAccountInfo.getQuery());
		      rAcDto.setManage(null==OffAccountInfo.getManage()||"".equals(OffAccountInfo.getManage()) ?"":OffAccountInfo.getManage());
		      rAcDto.setState(null==OffAccountInfo.getState()||"".equals(OffAccountInfo.getState()) ?"":OffAccountInfo.getState());
		      rAcDto.setSupusername(null==OffAccountInfo.getSupusername()||"".equals(OffAccountInfo.getSupusername()) ?"":OffAccountInfo.getSupusername());
		      rAcDto.setLevel(null==OffAccountInfo.getLevel()||"".equals(OffAccountInfo.getLevel()) ?"":OffAccountInfo.getLevel());
		      rAcDto.setOfftype(null==OffAccountInfo.getOfftype()||"".equals(OffAccountInfo.getOfftype()) ?"":OffAccountInfo.getOfftype());
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
	@RequestMapping(value = "/addOffAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public OffAccountResult addOffAccountInfo(@ApiParam(value = "Json参数", required = true) @RequestBody OffAccountInfo param) throws Exception {
		OffAccountResult result = new OffAccountResult();
		try {
			
			String username = param.getUsername();
			String ausername = param.getAusername();
			String password = param.getPassword();
			String level = param.getLevel();
			String supusername = param.getSupusername();
			Double percentage = null;
			Double limited =  null;
			Double ratio = null;
			
			//判断是否有权限新增下线
			if (level.equals("3")){
		        result.fail(MessageTool.Code_2004);
		        LOG.info(result.getMessage());
		        return result;	
			}
			
			if (null != param.getLimited())
				limited = param.getLimited();
			if (null != param.getRatio())
				ratio = param.getRatio();
			if (null != param.getLimited())
				limited = param.getLimited();
			if (null != param.getPercentage())
				percentage = param.getPercentage();
				
			//参数合规性校验，必要参数不能为空
			if (ToolsUtil.isEmptyTrim(username)||ToolsUtil.isEmptyTrim(password)){
			      result.fail("用户名，密码",MessageTool.Code_2002);
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
			//数字型
			System.out.println("7----------"+limited);
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
			
			//数字型
			if (null != percentage){
				if (ToolsUtil.isNumeric(String.valueOf(percentage))){
				      result.fail("抽成占比",MessageTool.Code_1004);
				      LOG.info(result.getMessage());
				      return result;		
				}
			}
			
			OffAccountInfo off = new OffAccountInfo();
			off.setUsername(supusername);
			OffAccountInfo OffAccountInfo = OffAccountInfoService.getOffAccountInfoByUser(off);
		    if(OffAccountInfo!=null){
				//洗码比逻辑 
		    	System.out.println("80----"+ratio+"..."+OffAccountInfo.getRatio());
				if (ratio>OffAccountInfo.getRatio()){
				      result.fail("洗码比",MessageTool.Code_1008);
				      LOG.info(result.getMessage());
				      return result;	
				}	
				//代理占比逻辑
				if (percentage>OffAccountInfo.getPercentage()){
				      result.fail("代理占成",MessageTool.Code_1008);
				      LOG.info(result.getMessage());
				      return result;	
				}
				
		    }
			param.setPassword(DigestUtils.md5Hex(password));
		    OffAccountInfo paraInfo = mapper.map(param, OffAccountInfo.class);
		    paraInfo.setQuery("Y1,Y2,Y3,Y4,Y5");
		    paraInfo.setManage("M1,M2,M3,M4,M5");
		    paraInfo.setState("1");//默认状态正常
		    paraInfo.setLevel(ToolsUtil.decideLevel(level));
		    paraInfo.setOfftype("1");
		    paraInfo.setInputdate(new Date());
		    
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
		    accountDetail.setUserid(paraInfo.getUserid());
		    accountDetail.setUsername(paraInfo.getUsername());
		    accountDetail.setLimited(paraInfo.getLimited());
		    accountDetail.setRatio(paraInfo.getRatio());
		    accountDetail.setPercentage(0.0);
		    accountDetail.setState("1");
		    accountDetail.setSupusername(paraInfo.getSupusername());
		    accountDetail.setLevel(paraInfo.getLevel());
		    accountDetail.setOfftype("1");
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
	@RequestMapping(value = "/updateOffAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public OffAccountResult updateOffAccountInfo(@ApiParam(value = "Json参数", required = true) @RequestBody OffAccountInfo param) throws Exception {
		OffAccountResult result = new OffAccountResult();
		try {
			
			String username = param.getUsername();
			String ausername = param.getAusername();
			String password = param.getPassword();
			String supusername = param.getSupusername();
			String level = param.getLevel();
			Double limited =  null;
			Double ratio = null;
			Double percentage = null;
			
			if (null != param.getLimited())
				limited = param.getLimited();
			if (null != param.getRatio())
				ratio = param.getRatio();
			if (null != param.getPercentage())
				percentage = param.getPercentage();
			
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
			
			//最长14个英文或者数字组合
			if (ToolsUtil.validatName(username)){
			      result.fail("用户名",MessageTool.Code_1006);
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
			System.out.println("7----------"+limited);
			if (null != limited){
				if (0.0!=limited){
				if (ToolsUtil.isNumeric(String.valueOf(limited))){
				      result.fail("点数限额",MessageTool.Code_1004);
				      LOG.info(result.getMessage());
				      return result;		
				}
				}
			}
			
			//数字型
			if (null != ratio){
				if (0.0!=ratio){
				if (ToolsUtil.isNumeric(String.valueOf(ratio))){
				      result.fail("洗码比",MessageTool.Code_1004);
				      LOG.info(result.getMessage());
				      return result;		
				}
				}
			}
			
			//数字型
			if (null != percentage){
				if (0.0!=percentage){
				if (ToolsUtil.isNumeric(String.valueOf(percentage))){
				      result.fail("抽成占比",MessageTool.Code_1004);
				      LOG.info(result.getMessage());
				      return result;		
				}
				}
			}
			
			/*
			//状态判断
			if (ToolsUtil.betweenRange(state)){
			      result.fail("状态",MessageTool.Code_1005);
			      LOG.info(result.getMessage());
			      return result;	
			}
			
			*/
			
			OffAccountInfo OffAccountInfo = OffAccountInfoService.getOffAccountInfo(param.getUserid());
			if(OffAccountInfo==null){
			      result.fail(MessageTool.FailCode);
			      LOG.info(result.getMessage());
			      return result;
			}
			

			password = DigestUtils.md5Hex(password);
		    OffAccountInfo paraInfo = mapper.map(param, OffAccountInfo.class);
		    OffAccountInfo  offaccountInfocheck = OffAccountInfoService.getOffAccountInfoByUserAndId(paraInfo);
		    if (offaccountInfocheck!=null){
			      result.fail(username,MessageTool.Code_2005);
			      LOG.info(result.getMessage());
			      return result;	
		    }
		    
		    paraInfo.setUsername(null==param.getUsername()||"".equals(param.getUsername()) ? OffAccountInfo.getUsername():param.getUsername());
		    paraInfo.setAusername(null==param.getAusername()||"".equals(param.getAusername()) ? OffAccountInfo.getAusername():param.getAusername());
		    System.out.println("800--"+param.getPassword());
		    System.out.println("800--"+OffAccountInfo.getPassword());
		    paraInfo.setPassword(null==param.getPassword()||"".equals(param.getPassword()) ? OffAccountInfo.getPassword():password);
		    paraInfo.setLimited(null==param.getLimited()||"".equals(param.getLimited())||0.0==param.getLimited() ? OffAccountInfo.getLimited():param.getLimited());
		    paraInfo.setRatio(null==param.getRatio()||"".equals(param.getRatio())||0.0==param.getRatio() ? OffAccountInfo.getRatio():param.getRatio());
		    paraInfo.setPercentage(null==param.getPercentage()||"".equals(param.getPercentage())||0.0==param.getPercentage() ? OffAccountInfo.getPercentage():param.getPercentage());
		    paraInfo.setLevel(null==param.getLevel()||"".equals(param.getLevel()) ? OffAccountInfo.getLevel():OffAccountInfo.getLevel());//代理级别不允许修改
		    paraInfo.setQuery(null==param.getQuery()||"".equals(param.getQuery()) ? OffAccountInfo.getQuery():OffAccountInfo.getQuery());
		    paraInfo.setManage(null==param.getManage()||"".equals(param.getManage()) ? OffAccountInfo.getManage():OffAccountInfo.getManage());
		    paraInfo.setOfftype(null==param.getOfftype()||"".equals(param.getOfftype()) ? OffAccountInfo.getOfftype():OffAccountInfo.getOfftype());
		    paraInfo.setState(null==param.getState()||"".equals(param.getState()) ?  OffAccountInfo.getState():param.getState());
		    paraInfo.setUpdateip(null==param.getUpdateip()||"".equals(param.getUpdateip()) ? OffAccountInfo.getUpdateip():param.getUpdateip());
		    //System.out.println("9-------------d-"+ToolsUtil.getCurrentTime());
		    paraInfo.setUpdatedate(new Date());
		    OffAccountInfoService.updateOffAccountInfo(paraInfo);
		    
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
		    
		    accountDetailService.updateAccountDetail(accountDetail);
		    
		    result.success();
			LOG.info(result.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		return result;
	}

	@ApiOperation(value = "获取用户列表", notes = "获取用户列表", httpMethod = "POST")
	@RequestMapping(value = "/getAllOffAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public OffAccountListResult getAllOffAccountInfo(@ApiParam(value = "Json参数", required = true) @RequestBody OffAccountInfo param) throws Exception {
	    OffAccountListResult result = new OffAccountListResult();
		try {
			List<OffAccountInfo> OffAccountInfos = OffAccountInfoService.getAllOffAccountInfo(param);
			if(OffAccountInfos==null){
			      result.fail(MessageTool.FailCode);
			      LOG.info(result.getMessage());
			      return result;
			}
			List<OffAccountDto> list = new ArrayList<OffAccountDto>();
			for (int i = 0;i<OffAccountInfos.size();i++){
			  OffAccountDto rAcDto = new OffAccountDto();		        
		      rAcDto.setUserid(null==OffAccountInfos.get(i).getUserid()||"".equals(OffAccountInfos.get(i).getUserid())||0==OffAccountInfos.get(i).getUserid() ?0:OffAccountInfos.get(i).getUserid());
		      rAcDto.setUsername(null==OffAccountInfos.get(i).getUsername()||"".equals(OffAccountInfos.get(i).getUsername()) ?"":OffAccountInfos.get(i).getUsername());
		      rAcDto.setAusername(null==OffAccountInfos.get(i).getAusername()||"".equals(OffAccountInfos.get(i).getAusername()) ?"":OffAccountInfos.get(i).getAusername());
		      rAcDto.setPassword(null==OffAccountInfos.get(i).getPassword()||"".equals(OffAccountInfos.get(i).getPassword()) ?"":OffAccountInfos.get(i).getPassword());
		      rAcDto.setLimited(null==OffAccountInfos.get(i).getLimited()||"".equals(OffAccountInfos.get(i).getLimited())||0.0==OffAccountInfos.get(i).getLimited() ?0.0:OffAccountInfos.get(i).getLimited());
		      rAcDto.setRatio(null==OffAccountInfos.get(i).getRatio()||"".equals(OffAccountInfos.get(i).getRatio())||0.0==OffAccountInfos.get(i).getRatio() ?0.0:OffAccountInfos.get(i).getRatio());
		      rAcDto.setPercentage(null==OffAccountInfos.get(i).getPercentage()||"".equals(OffAccountInfos.get(i).getPercentage())||0.0==OffAccountInfos.get(i).getPercentage() ?0.0:OffAccountInfos.get(i).getPercentage());
		      rAcDto.setQuery(null==OffAccountInfos.get(i).getQuery()||"".equals(OffAccountInfos.get(i).getQuery()) ?"":OffAccountInfos.get(i).getQuery());
		      rAcDto.setManage(null==OffAccountInfos.get(i).getManage()||"".equals(OffAccountInfos.get(i).getManage()) ?"":OffAccountInfos.get(i).getManage());
		      rAcDto.setState(null==OffAccountInfos.get(i).getState()||"".equals(OffAccountInfos.get(i).getState()) ?"":OffAccountInfos.get(i).getState());
		      rAcDto.setSupusername(null==OffAccountInfos.get(i).getSupusername()||"".equals(OffAccountInfos.get(i).getSupusername()) ?"":OffAccountInfos.get(i).getSupusername());
		      rAcDto.setLevel(null==OffAccountInfos.get(i).getLevel()||"".equals(OffAccountInfos.get(i).getLevel()) ?"":OffAccountInfos.get(i).getLevel());
		      rAcDto.setOfftype(null==OffAccountInfos.get(i).getOfftype()||"".equals(OffAccountInfos.get(i).getOfftype()) ?"":OffAccountInfos.get(i).getOfftype());
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
