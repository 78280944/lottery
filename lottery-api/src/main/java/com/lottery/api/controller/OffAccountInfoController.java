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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lottery.api.dto.AccountInfoVo;
import com.lottery.api.dto.LoginParamVo;
import com.lottery.api.dto.OffAccountInfoVo;
import com.lottery.api.dto.UpdateAccountPerVo;
import com.lottery.api.dto.UpdateAccountRatioVo;
import com.lottery.api.dto.UpdateAccountRiskVo;
import com.lottery.api.dto.UpdateOffAccountVo;
import com.lottery.api.dto.UpdatePlayAmountVo;
import com.lottery.api.dto.UpdatePlayPassVo;
import com.lottery.api.dto.UpdatePlayRatioVo;
import com.lottery.api.util.ToolsUtil;
import com.lottery.orm.bo.AccountDetail;
import com.lottery.orm.bo.AccountInfo;
import com.lottery.orm.bo.OffAccountInfo;
import com.lottery.orm.dao.AccountDetailMapper;
import com.lottery.orm.dao.AccountInfoMapper;
import com.lottery.orm.dao.OffAccountInfoMapper;
import com.lottery.orm.dto.OffAccountDto;
import com.lottery.orm.result.OffAccountListResult;
import com.lottery.orm.result.OffAccountResult;
import com.lottery.orm.result.RestResult;
import com.lottery.orm.service.AccountInfoService;
import com.lottery.orm.service.OffAccountInfoService;
import com.lottery.orm.util.EnumType;
import com.lottery.orm.util.MessageTool;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RequestMapping(value = "/offAccount", produces = {"application/json;charset=UTF-8"})
@Api(value = "/offAccount", description = "代理帐号信息接口")
@Controller
public class OffAccountInfoController {
	public static final Logger LOG = Logger.getLogger(OffAccountInfoController.class);
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
    private AccountInfoService accountInfoService;
	
	@Autowired
    private OffAccountInfoService OffAccountInfoService;
	
	@Autowired
	private OffAccountInfoMapper offAccountInfoMapper;
	
	@Autowired
    private AccountInfoMapper accountInfoMapper;
	
	@Autowired
    private AccountDetailMapper accountDetailMapper;
	
	@ApiOperation(value = "获取代理信息", notes = "获取代理信息", httpMethod = "POST")
	@RequestMapping(value = "/getOffAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public OffAccountResult getOffAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody LoginParamVo param) throws Exception {
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
		    OffAccountInfo OffAccountInfo = offAccountInfoMapper.selectByLogin(paraInfo);
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
		      rAcDto.setRiskamount(null==OffAccountInfo.getRiskamount()||"".equals(OffAccountInfo.getRiskamount()) ?"":OffAccountInfo.getRiskamount());
		      result.success(rAcDto);
		    }else{
		    	result.fail(MessageTool.Code_3001);
		    }
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;

	}
	
	@ApiOperation(value = "新增代理", notes = "新增代理", httpMethod = "POST")
	@RequestMapping(value = "/addOffAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public RestResult addOffAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody OffAccountInfoVo param) throws Exception {
		RestResult result = new RestResult();
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
			param.setPassword(DigestUtils.md5Hex(password));
			OffAccountInfo paraInfo = mapper.map(param, OffAccountInfo.class);
			OffAccountInfo OffAccountInfo = offAccountInfoMapper.selectByUsername(paraInfo.getUsername());
			//System.out.println("9--------"+OffAccountInfo+"..."+OffAccountInfo.getUsername());
		    if(OffAccountInfo!=null){ 	
		    	result.fail(username,MessageTool.Code_2005);
		    }else{
				//洗码比逻辑 
		    	OffAccountInfo = offAccountInfoMapper.selectByUsername(paraInfo.getSupusername());
		    	if (OffAccountInfo==null){
		    		result.fail(username,MessageTool.Code_2005);
		    		return result;
		    	}
		    	//System.out.println("80----"+ratio+"..."+OffAccountInfo.getRatio());
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

			    paraInfo.setQuery("Y1,Y2,Y3,Y4,Y5");
			    paraInfo.setManage("M1,M2,M3,M4,M5");
			    paraInfo.setState("1");//默认状态正常
			    paraInfo.setLevel(ToolsUtil.decideLevel(level));
			    paraInfo.setOfftype("1");
			    paraInfo.setInputdate(new Date());
			    paraInfo.setRiskamount(param.getRiskamount());
			    OffAccountInfoService.addOffAccountInfo(paraInfo);
			    
			    result.success();
		    }

			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	/*
	@ApiOperation(value = "修改代理", notes = "修改代理", httpMethod = "POST")
	@RequestMapping(value = "/updateOffAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public RestResult updateOffAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdateOffAccountVo param) throws Exception {
		RestResult result = new RestResult();
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
			
			
			//6-14位数字、字母、符号组合
			if (ToolsUtil.validateSignName(password)){
			      result.fail("密码",MessageTool.Code_1007);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			
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
			
			
			//状态判断
			if (ToolsUtil.betweenRange(state)){
			      result.fail("状态",MessageTool.Code_1005);
			      LOG.info(result.getMessage());
			      return result;	
			}
			
			
			
			OffAccountInfo OffAccountInfo = offAccountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(OffAccountInfo==null){
				result.fail(MessageTool.Code_3001);
			}else{
				password = DigestUtils.md5Hex(password);
			    OffAccountInfo paraInfo = mapper.map(param, OffAccountInfo.class);
			    OffAccountInfo  offaccountInfocheck = offAccountInfoMapper.selectByUserAndId(paraInfo);
			    if (offaccountInfocheck!=null){
				      result.fail(username,MessageTool.Code_2005);
			    }else{
				    paraInfo.setUsername(null==param.getUsername()||"".equals(param.getUsername()) ? OffAccountInfo.getUsername():param.getUsername());
				    paraInfo.setAusername(null==param.getAusername()||"".equals(param.getAusername()) ? OffAccountInfo.getAusername():param.getAusername());
				    System.out.println("800--"+param.getPassword());
				    System.out.println("800--"+OffAccountInfo.getPassword());
				    paraInfo.setPassword(null==param.getPassword()||"".equals(param.getPassword()) ? OffAccountInfo.getPassword():password);
				    paraInfo.setLimited(null==param.getLimited()||"".equals(param.getLimited())||0.0==param.getLimited() ? OffAccountInfo.getLimited():param.getLimited());
				    paraInfo.setRatio(null==param.getRatio()||"".equals(param.getRatio())||0.0==param.getRatio() ? OffAccountInfo.getRatio():param.getRatio());
				    paraInfo.setPercentage(null==param.getPercentage()||"".equals(param.getPercentage())||0.0==param.getPercentage() ? OffAccountInfo.getPercentage():param.getPercentage());
				    paraInfo.setLevel(null==param.getLevel()||"".equals(param.getLevel()) ? OffAccountInfo.getLevel():OffAccountInfo.getLevel());//代理级别不允许修改
				    paraInfo.setQuery("Y1,Y2,Y3,Y4,Y5");
				    paraInfo.setManage("M1,M2,M3,M4,M5");
				    paraInfo.setOfftype(null==param.getOfftype()||"".equals(param.getOfftype()) ? OffAccountInfo.getOfftype():OffAccountInfo.getOfftype());
				    paraInfo.setState(null==param.getState()||"".equals(param.getState()) ?  OffAccountInfo.getState():param.getState());
				    paraInfo.setUpdateip(null==param.getIp()||"".equals(param.getIp()) ? OffAccountInfo.getIp():param.getIp());
				    //System.out.println("9-------------d-"+ToolsUtil.getCurrentTime());
				    paraInfo.setUpdatedate(new Date());
				    OffAccountInfoService.updateOffAccountInfo(paraInfo);
				    result.success();
			    }
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}

	*/
	@ApiOperation(value = "获取该代理下的代理列表", notes = "获取该代理下的代理列表", httpMethod = "POST")
	@RequestMapping(value = "/getAllOffAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public OffAccountListResult getAllOffAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody AccountInfoVo param) throws Exception {
	    OffAccountListResult result = new OffAccountListResult();
		try {
			
			OffAccountInfo offacount = offAccountInfoMapper.selectByPrimaryKey(param.getUserid());
			//OffAccountInfo offacount = offAccountInfoMapper.selectByUseridAndType(param.getUserid(), EnumType.OffType.Agency.ID);
			if(offacount==null){
				  result.fail(MessageTool.Code_3001);
			      LOG.info(result.getMessage());
			      return result;
			}
			List<OffAccountInfo> OffAccountInfos = offAccountInfoMapper.selectBySupusername(offacount.getUsername(), EnumType.OffType.Agency.ID,param.getBeginRow(), param.getPageSize());
			
			List<OffAccountDto> list = new ArrayList<OffAccountDto>();
			for (int i = 0;i<OffAccountInfos.size();i++){
			  AccountDetail accountDetail =  accountDetailMapper.selectByUserId(OffAccountInfos.get(i).getUserid(),OffAccountInfos.get(i).getOfftype());
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
		      rAcDto.setAccountID(accountDetail.getAccountid());
		      rAcDto.setAccountAmount(accountDetail.getMoney());
		      rAcDto.setRiskamount(null==OffAccountInfos.get(i).getRiskamount()||"".equals(OffAccountInfos.get(i).getRiskamount()) ?"":OffAccountInfos.get(i).getRiskamount());
		      list.add(rAcDto);  
			}
		    result.success(list);
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	

	@ApiOperation(value = "代理用户修改玩家剩余点数", notes = "代理用户修改玩家剩余点数", httpMethod = "POST")
	@RequestMapping(value = "/updatePlayAmount", method = RequestMethod.POST)
	@ResponseBody
	public RestResult updateAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdatePlayAmountVo param) throws Exception {
		RestResult result = new RestResult();
		try {
			int userid = param.getUserid();
			int accountid = param.getAccountid();
			BigDecimal accountamount = param.getAccountamount();
            String supusername = param.getSupusername();
            int offtype = param.getOfftype();
			String ip = param.getIp();
			
			if (0==userid||0==accountid){
			      result.fail("用户ID或者账户ID",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			AccountInfo accountInfo = accountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(accountInfo==null){
			      result.fail(MessageTool.Code_3001);
			}else{
			    AccountDetail accountDetail = accountDetailMapper.selectByPrimaryKey(accountid);
			    if (accountDetail==null){
			    	result.fail(MessageTool.Code_3001);	
			    }
			    accountDetail.setMoney(accountamount);
			    accountInfoService.updateAccountMount(accountDetail);
			    LOG.info("修改剩余点数记录详情为："+" 管理员："+supusername+" 账户类型："+offtype+" IP："+ip+" 修改玩家ID"+userid+" 剩余点数修改为"+accountamount);
			    result.success();
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	
	@ApiOperation(value = "代理用户修改玩家密码", notes = "代理用户修改玩家密码", httpMethod = "POST")
	@RequestMapping(value = "/updatePlayPass", method = RequestMethod.POST)
	@ResponseBody
	public RestResult updateAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdatePlayPassVo param) throws Exception {
		RestResult result = new RestResult();
		try {
			int userid = param.getUserid();
			String password = param.getPassword();
            String supusername = param.getSupusername();
            int offtype = param.getOfftype();
			String ip = param.getIp();
			
			if (0==userid){
			      result.fail("用户ID",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			AccountInfo accountInfo = accountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(accountInfo==null){
			      result.fail(MessageTool.Code_3001);
			}else{
				accountInfo.setPassword(DigestUtils.md5Hex(password));
			    accountInfoService.updateAccountInfo(accountInfo);
			    LOG.info("修改密码记录详情为："+" 管理员："+supusername+" 账户类型："+offtype+" IP："+ip+" 修改玩家ID"+userid+" 密码修改为"+accountInfo.getPassword());
			    result.success();
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	@ApiOperation(value = "代理用户修改玩家洗码比", notes = "代理用户修改玩家洗码比", httpMethod = "POST")
	@RequestMapping(value = "/updatePlayRatio", method = RequestMethod.POST)
	@ResponseBody
	public RestResult updatePlayRatio(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdatePlayRatioVo param) throws Exception {
		RestResult result = new RestResult();
		try {
			int userid = param.getUserid();
			double ratio = param.getRatio();
            String supusername = param.getSupusername();
            int offtype = param.getOfftype();
			String ip = param.getIp();
			
			if (0==userid){
			      result.fail("用户ID",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			AccountInfo accountInfo = accountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(accountInfo==null){
			      result.fail(MessageTool.Code_3001);
			}else{
				accountInfo.setRatio(ratio);
			    accountInfoService.updateAccountInfo(accountInfo);
			    LOG.info("修改玩家洗码比记录详情为："+" 管理员："+supusername+" 账户类型："+offtype+" IP："+ip+" 修改玩家ID"+userid+" 玩家洗码比修改为"+accountInfo.getRatio());
			    result.success();
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	
	@ApiOperation(value = "代理用户修改下线剩余点数", notes = "代理用户修改下线剩余点数", httpMethod = "POST")
	@RequestMapping(value = "/updateAccountAmount", method = RequestMethod.POST)
	@ResponseBody
	public RestResult updateOffAccountInfo(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdatePlayAmountVo param) throws Exception {
		RestResult result = new RestResult();
		try {
			int userid = param.getUserid();
			int accountid = param.getAccountid();
			BigDecimal accountamount = param.getAccountamount();
            String supusername = param.getSupusername();
            int offtype = param.getOfftype();
			String ip = param.getIp();
			
			if (0==userid||0==accountid){
			      result.fail("用户ID或者账户ID",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			OffAccountInfo offAccountInfo = offAccountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(offAccountInfo==null){
			      result.fail(MessageTool.Code_3001);
			}else{
			    AccountDetail accountDetail = accountDetailMapper.selectByPrimaryKey(accountid);
			    if (accountDetail==null){
			    	result.fail(MessageTool.Code_3001);	
			    }
			    accountDetail.setMoney(accountamount);
			    accountInfoService.updateAccountMount(accountDetail);
			    LOG.info("修改剩余点数记录详情为："+" 管理员："+supusername+" 账户类型："+offtype+" IP："+ip+" 修改下线ID"+userid+" 剩余点数修改为"+accountamount);
			    result.success();
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	
	@ApiOperation(value = "代理用户修改下线密码", notes = "代理用户修改下线密码", httpMethod = "POST")
	@RequestMapping(value = "/updateAccountPass", method = RequestMethod.POST)
	@ResponseBody
	public RestResult updateAccountPass(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdatePlayPassVo param) throws Exception {
		RestResult result = new RestResult();
		try {
			int userid = param.getUserid();
			String password = param.getPassword();
            String supusername = param.getSupusername();
            int offtype = param.getOfftype();
			String ip = param.getIp();
			
			if (0==userid){
			      result.fail("用户ID",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			OffAccountInfo offAccountInfo = offAccountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(offAccountInfo==null){
			      result.fail(MessageTool.Code_3001);
			}else{
				offAccountInfo.setPassword(DigestUtils.md5Hex(password));
				offAccountInfoMapper.updateByPrimaryKey(offAccountInfo);
			    LOG.info("修改密码记录详情为："+" 管理员："+supusername+" 账户类型："+offtype+" IP："+ip+" 修改下家ID"+userid+" 密码修改为"+offAccountInfo.getPassword());
			    result.success();
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	
	@ApiOperation(value = "代理用户修改下线风险限额", notes = "代理用户修改下线风险限额", httpMethod = "POST")
	@RequestMapping(value = "/updateAccountRisk", method = RequestMethod.POST)
	@ResponseBody
	public RestResult updateAccountRisk(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdateAccountRiskVo param) throws Exception {
		RestResult result = new RestResult();
		try {
			int userid = param.getUserid();
			String riskamount = param.getRiskamount();
            String supusername = param.getSupusername();
            int offtype = param.getOfftype();
			String ip = param.getIp();
			
			if (0==userid){
			      result.fail("用户ID",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			OffAccountInfo offAccountInfo = offAccountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(offAccountInfo==null){
			      result.fail(MessageTool.Code_3001);
			}else{
				offAccountInfo.setRiskamount(riskamount);
				offAccountInfoMapper.updateByPrimaryKey(offAccountInfo);
			    LOG.info("修改风险限额记录详情为："+" 管理员："+supusername+" 账户类型："+offtype+" IP："+ip+" 修改下家ID"+userid+" 风险限额修改为"+offAccountInfo.getRiskamount());
			    result.success();
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	@ApiOperation(value = "代理用户修改下线代理占成", notes = "代理用户修改下线代理占成", httpMethod = "POST")
	@RequestMapping(value = "/updateAccountPercent", method = RequestMethod.POST)
	@ResponseBody
	public RestResult updateAccountPercent(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdateAccountPerVo param) throws Exception {
		RestResult result = new RestResult();
		try {
			int userid = param.getUserid();
			double percentage = param.getPetcentage();
            String supusername = param.getSupusername();
            int offtype = param.getOfftype();
			String ip = param.getIp();
			
			if (0==userid){
			      result.fail("用户ID",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			OffAccountInfo offAccountInfo = offAccountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(offAccountInfo==null){
			      result.fail(MessageTool.Code_3001);
			}else{
				offAccountInfo.setPercentage(percentage);;
				offAccountInfoMapper.updateByPrimaryKey(offAccountInfo);
			    LOG.info("修改代理占成记录详情为："+" 管理员："+supusername+" 账户类型："+offtype+" IP："+ip+" 修改下家ID"+userid+" 代理占成修改为"+offAccountInfo.getPercentage());
			    result.success();
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
	
	@ApiOperation(value = "代理用户修改下线洗码比", notes = "代理用户修改下线代洗码比", httpMethod = "POST")
	@RequestMapping(value = "/updateAccountRatio", method = RequestMethod.POST)
	@ResponseBody
	public RestResult updateAccountRatio(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdateAccountRatioVo param) throws Exception {
		RestResult result = new RestResult();
		try {
			int userid = param.getUserid();
			double ratio = param.getRatio();
            String supusername = param.getSupusername();
            int offtype = param.getOfftype();
			String ip = param.getIp();
			
			if (0==userid){
			      result.fail("用户ID",MessageTool.Code_2002);
			      LOG.info(result.getMessage());
			      return result;
			}
			
			OffAccountInfo offAccountInfo = offAccountInfoMapper.selectByPrimaryKey(param.getUserid());
			if(offAccountInfo==null){
			      result.fail(MessageTool.Code_3001);
			}else{
				offAccountInfo.setRatio(ratio);;
				offAccountInfoMapper.updateByPrimaryKey(offAccountInfo);
			    LOG.info("修改洗码比记录详情为："+" 管理员："+supusername+" 账户类型："+offtype+" IP："+ip+" 修改下家ID"+userid+" 代理占成修改为"+offAccountInfo.getRatio());
			    result.success();
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(),e);
		}
		return result;
	}
}
