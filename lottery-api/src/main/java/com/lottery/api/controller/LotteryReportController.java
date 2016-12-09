package com.lottery.api.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lottery.api.dto.ReportParamVo;
import com.lottery.orm.dao.LotteryReportMapper;
import com.lottery.orm.dto.InoutReportDto;
import com.lottery.orm.dto.TradeReportDto;
import com.lottery.orm.dto.WinningReportDto;
import com.lottery.orm.result.InoutReportResult;
import com.lottery.orm.result.TradeReportResult;
import com.lottery.orm.result.WinningReportResult;
import com.lottery.orm.util.MessageTool;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RequestMapping(value = "/report", produces = { "application/json;charset=UTF-8" })
@Api(value = "/report", description = "报表信息接口")
@Controller
public class LotteryReportController {
	public static final Logger LOG = Logger.getLogger(LotteryReportController.class);

	@Autowired
	LotteryReportMapper lotteryReportMapper;

	@ApiOperation(value = "获取输赢报表", notes = "获取输赢报表", httpMethod = "POST")
	@RequestMapping(value = "/getWinningReport", method = RequestMethod.POST)
	@ResponseBody
	public WinningReportResult getWinningReport(
			@ApiParam(value = "Json参数", required = true) @Validated @RequestBody ReportParamVo param) throws Exception {
		WinningReportResult result = new WinningReportResult();
		try {
			List<WinningReportDto> accountList = lotteryReportMapper.selectByWinningReport(param.getStartTime(),
					param.getEndTime(), param.getBeginRow(), param.getPageSize());
			result.success(accountList);
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(), e);
		}
		return result;

	}

	@ApiOperation(value = "获取点数出入报表", notes = "获取点数出入报表", httpMethod = "POST")
	@RequestMapping(value = "/getInoutReport", method = RequestMethod.POST)
	@ResponseBody
	public InoutReportResult getInoutReport(
			@ApiParam(value = "Json参数", required = true) @Validated @RequestBody ReportParamVo param) throws Exception {
		InoutReportResult result = new InoutReportResult();
		try {
			List<InoutReportDto> accountList = lotteryReportMapper.selectByInoutReport(param.getStartTime(),
					param.getEndTime(), param.getAccountId(), param.getBeginRow(), param.getPageSize());
			result.success(accountList);
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(), e);
		}
		return result;

	}

	@ApiOperation(value = "获取交易报表", notes = "获取交易报表", httpMethod = "POST")
	@RequestMapping(value = "/getTradeReport", method = RequestMethod.POST)
	@ResponseBody
	public TradeReportResult getTradeReport(
			@ApiParam(value = "Json参数", required = true) @Validated @RequestBody ReportParamVo param) throws Exception {
		TradeReportResult result = new TradeReportResult();
		try {
			List<TradeReportDto> accountList = lotteryReportMapper.selectByTradeReport(param.getStartTime(),
					param.getEndTime(), param.getAccountId(), param.getBeginRow(), param.getPageSize());
			result.success(accountList);
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(), e);
		}
		return result;

	}

}
